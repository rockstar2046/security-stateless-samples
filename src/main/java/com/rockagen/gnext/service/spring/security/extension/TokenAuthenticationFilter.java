/*
 * Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rockagen.gnext.service.spring.security.extension;

import com.rockagen.commons.util.CommUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Token authentication filter
 * <p>This filter will get a token("X-AUTH-TOKEN" default) from header at first,if token validated,
 * identity will be authenticated,otherwise return 401 status </p>
 * @author ra
 * @since JDK1.8
 */
public class TokenAuthenticationFilter extends GenericFilterBean {

    private String tokenName ="X-AUTH-TOKEN";

    private ExTokenAuthentication exTokenAuthentication;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String token=req.getHeader(tokenName);
        if(CommUtil.isNotBlank(token)){
            //Validate token
            UserDetails user=exTokenAuthentication.getUserDetailsFromToken(token);
            if(user!=null){
                // authenticated
                UsernamePasswordAuthenticationToken authentication=new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword(),user.getAuthorities());
                authentication.setDetails(new ExWebAuthenticationDetails(req));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        chain.doFilter(request, response);
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public void setExTokenAuthentication(ExTokenAuthentication exTokenAuthentication) {
        this.exTokenAuthentication = exTokenAuthentication;
    }
}
