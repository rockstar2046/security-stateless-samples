/*
 * Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rockagen.gnext.service.spring.security.extension;

import com.rockagen.gnext.po.AuthResource;
import com.rockagen.gnext.po.AuthRole;
import com.rockagen.gnext.service.AuthResourceServ;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 *
 * Extension implementation of <tt>FilterInvocationDefinitionSource</tt>.
 * <p>
 * Stores an ordered map of {@link RequestMatcher}s to <tt>ConfigAttribute</tt> collections and provides matching
 * of {@code FilterInvocation}s against the items stored in the map.
 * <p>
 * The order of the {@link RequestMatcher}s in the map is very important. The <b>first</b> one which matches the
 * request will be used. Later matchers in the map will not be invoked if a match has already been found.
 * Accordingly, the most specific matchers should be registered first, with the most general matches registered last.
 * <p>
 * The most common method creating an instance is using the Spring Security namespace. For example, the {@code pattern}
 * and {@code access} attributes of the {@code &lt;intercept-url&gt;} elements defined as children of the
 * {@code &lt;http&gt;} element are combined to build the instance used by the {@code FilterSecurityInterceptor}.
 *
 * <code>This need some like:</code>
 * <pre>
 * 	PATH         | ROLE
 *	-------------+-------------
 * 	/xx/root/x   | ROLE_ROOT
 * 	/xx/admin/x  | ROLE_ADMIN
 * 	/xx/auth/x   | ROLE_AUTH
 *	-------------+-------------
 * </pre>
 * <p>
 *  PATH used regular expression
 * </p>
 * @author Ben Alex
 * @author Luke Taylor
 * @author ra
 * @since JDK1.8
 * @since 3.0
 */
public class ExFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource{

    private static final Logger log = LoggerFactory.getLogger(ExFilterInvocationSecurityMetadataSource.class);
    private AuthResourceServ authResourceServ;

    private Cache cache;
    
    private Map<RequestMatcher, Collection<ConfigAttribute>> processMap() {


        Map<RequestMatcher, Collection<ConfigAttribute>> requestToExpressionAttributesMap =
                new LinkedHashMap<>();

        List<AuthResource> resources=authResourceServ.findAll();

        if(resources!=null&&resources.size()>0) {
            // Sort by priority
            // Jdk8 only
            resources.stream().sorted((a, b) -> a.getPriority().compareTo(b.getPriority())).forEach(x->{

                RequestMatcher request = new RegexRequestMatcher(x.getPath(), null);
                Set<AuthRole> roles = x.getRoles();
                List<ConfigAttribute> attrs = new ArrayList<>(roles.size());
                roles.forEach(y -> attrs.add(new SecurityConfig(y.getName().trim())));

                requestToExpressionAttributesMap.put(request, attrs);

            });

        }
        return requestToExpressionAttributesMap;
    }


    /**
     * Get Attributes map from cache if cache set.
     * @return Map
     */
    @SuppressWarnings("unchecked")
    private Map<RequestMatcher, Collection<ConfigAttribute>> getAttributesMap(){
        
        if(cache!=null){
            Map<RequestMatcher, Collection<ConfigAttribute>> map;
            String key="ex.securityMetadataSource";
            Element e=cache.get(key);
            if(e!=null && !e.isExpired()){
                log.debug("Cache [Hit] ex.securityMetadataSource: {}",e);
                map= (Map<RequestMatcher, Collection<ConfigAttribute>>)e.getObjectValue();
            }else{
                map=processMap();
                Element enew = new Element(key, map);
                cache.put(enew);
                log.debug("Cache [Update] ex.securityMetadataSource: {}",enew);
            }
            return map;
        }
        return processMap();
        
    }


    //~ Methods ========================================================================================================

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        Set<ConfigAttribute> allAttributes = new HashSet<>();

        for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : getAttributesMap().entrySet()) {
            allAttributes.addAll(entry.getValue());
        }

        return allAttributes;
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) {
        final HttpServletRequest request = ((FilterInvocation) object).getRequest();
        for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : getAttributesMap().entrySet()) {
            if (entry.getKey().matches(request)) {
                return entry.getValue();
            }
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }


    public void setAuthResourceServ(AuthResourceServ authResourceServ) {
        this.authResourceServ = authResourceServ;
    }

    public void setCache(Cache cache) {
        this.cache = cache;
    }
}
