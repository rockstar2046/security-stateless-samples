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
package com.rockagen.gnext.Filter;

import com.rockagen.commons.util.CommUtil;
import com.rockagen.gnext.po.nosql.AccessLog;
import com.rockagen.gnext.tool.Conf;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Access Filter
 * @author ra
 * @since JDK1.8
 */
public class AccessFilter extends OncePerRequestFilter {

    private MongoTemplate mongoTemplate;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        accessLog(request);
        filterChain.doFilter(request, response);
    }

    /**
     * Save access log
     * @param request {@link HttpServletRequest}
     */
    private void accessLog(HttpServletRequest request) {

        AccessLog al=new AccessLog();
        al.setIp(getIp(request));
        al.setMethod(request.getMethod());
        al.setReferer(request.getHeader("Referer"));
        al.setTimeAt(new Date());
        al.setUrl(request.getRequestURL().toString());
        al.setUserAgent(request.getHeader("User-Agent"));
        // Translate params
        Map<String,String> params=translateParaeters(request.getParameterMap());
        // protect sensitive value
        filterParameters(params);
        al.setParams(params);
        mongoTemplate.save(al);
        
    }

    /**
     * Translate value
     * @param params params map
     */
    private Map<String,String> translateParaeters(Map<String, String[]> params) {
        Map<String,String> map=new LinkedHashMap<>();
        for(Map.Entry<String ,String[]> x : params.entrySet()){
            map.put(x.getKey(), Arrays.toString(x.getValue()));
        }
        return map;
    }


    /**
     * Protect sensitive value
     * @param params  params map
     */
    private void filterParameters(Map<String,String> params){
        //key is "application.config.filter.parameters"
        // Value split by ","
        String filterParams=Conf.get("application.config.filter.parameters");
        if(CommUtil.isNotBlank(filterParams)){
            for(String x : filterParams.split(",")){
                if(params.containsKey(x)){
                    params.put(x, "[PROTECTED]");
                }
            }
        }
        
    }


    /**
     * Get remote ip
     * @param request {@link HttpServletRequest}
     * @return real ip address
     */
    private String getIp(HttpServletRequest request){

        String customerIp;
        // If proxies by nginx apache ...
        String temp1 = request.getHeader("X-Real-IP");

        if (!CommUtil.isBlank(temp1)) {
            customerIp = temp1;
        } else {
            // use default
            customerIp = request.getRemoteAddr();
        }
        return customerIp;
        
        
    }

    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
}
