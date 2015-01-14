/*
 * Copyright 2014 the original author or authors.
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

import com.rockagen.commons.util.CommUtil;
import com.rockagen.gnext.enums.UserReferer;
import com.rockagen.gnext.po.AuthUser;
import com.rockagen.gnext.service.AuthUserServ;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * Handle authentication if successful
 *
 * @author RA
 * @since 3.0
 */
public class ExAuthenticationSuccessHandler implements AuthenticationSuccessHandler {


    /**
     * HttpServletRequest Header params
     */
    private String userReferer = "X-Referer";
    private String tokenName = "X-AUTH-TOKEN";

    private AuthUserServ authUserServ;

    private ExTokenAuthentication exTokenAuthentication;

    //~ Methods ==================================================

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
        register(request, authentication);
        handle(response, authentication);
    }

    /**
     * Handle response
     *
     * @param response       response
     * @param authentication {@link org.springframework.security.core.Authentication}
     */
    private void handle(HttpServletResponse response, Authentication authentication) {

        String uid = authentication.getName();
        // Response Token
        String token = exTokenAuthentication.newToken(uid);
        if (CommUtil.isNotBlank(token)) {
            response.setHeader(tokenName, token);
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }


    /**
     * Register visitor
     *
     * @param request        request
     * @param authentication {@link org.springframework.security.core.Authentication}
     */
    protected void register(HttpServletRequest request, Authentication authentication) {

        String currentIp = new ExWebAuthenticationDetails(request).getRemoteAddress();
        String username = authentication.getName();

        AuthUser user = authUserServ.load(username);
        if(user==null)
            return;
        Date now = new Date();
        user.setLastSignInAt(user.getCurrentSignInAt());
        user.setCurrentSignInAt(now);
        user.setLastSignInIp(user.getCurrentSignInIp());
        user.setCurrentSignInIp(currentIp);
        Integer signCount=user.getSignInCount();
        if(signCount==null){
            signCount=0;
        }
        user.setSignInCount(++signCount);
        user.setFailedAttempts(0);

        // Referer
        String referer = request.getHeader(userReferer);
        user.setLastReferer(getReferer(referer));
        authUserServ.add(user);
    }

    private UserReferer getReferer(String referer) {
        UserReferer state = UserReferer.UNKNOWN;
        try {
            state = UserReferer.valueOf(referer);
        } catch (Exception e) {
            // DO NOT
        }
        return state;
    }


    public void setExTokenAuthentication(ExTokenAuthentication exTokenAuthentication) {
        this.exTokenAuthentication = exTokenAuthentication;
    }

    public void setUserReferer(String userReferer) {
        this.userReferer = userReferer;
    }

    public void setAuthUserServ(AuthUserServ authUserServ) {
        this.authUserServ = authUserServ;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }
}
