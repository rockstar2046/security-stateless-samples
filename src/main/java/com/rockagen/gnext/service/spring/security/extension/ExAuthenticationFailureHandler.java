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

import com.rockagen.gnext.enums.UserReferer;
import com.rockagen.gnext.po.AuthUser;
import com.rockagen.gnext.service.AuthUserServ;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * Handle authentication if Failure
 *
 * @author RA
 * @since 3.0
 */
public class ExAuthenticationFailureHandler implements AuthenticationFailureHandler {

    //~ Instance fields ==================================================

    private MessageSourceAccessor messages = SpringSecurityMessageSource
            .getAccessor();

    /**
     * HttpServletRequest param
     */
    private String username = "uid";


    /**
     * HttpServletRequest Header params
     */
    private String userReferer = "X-Referer";

    private int maxFailedAttempts = 6;

    // 3 hours
    private long lockedTime = 10800000;

    private AuthUserServ authUserServ;


    //~ Methods ==================================================


    /**
     * Implements locked user
     *
     * @param request   request
     * @param response  response
     * @param exception exception
     * @throws IOException      if an I/O error occurs
     * @throws ServletException if a servlet error occurs
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        if (exception instanceof BadCredentialsException) {
            exception = register(request);
        }
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication Failed: " + exception.getMessage());
    }

    /**
     * Register visitor
     *
     * @param request request
     * @return AuthenticationException
     */
    protected AuthenticationException register(HttpServletRequest request) {

        String username = request.getParameter(this.username);
        AuthUser user = authUserServ.load(username);
        if (user != null && user.enabled()) {
            Date date = new Date();
            if (user.getFailedAttempts()!=null && user.getFailedAttempts() >= maxFailedAttempts) {
                long now = date.getTime();
                long lastTime = user.getLastSignInAt().getTime();

                if (now - lastTime <= lockedTime) {
                    // Locked user
                    user.setLockedAt(date);
                    user.setEnabled(0);

                    return new DisabledException(messages.getMessage(
                            "AccountStatusUserDetailsChecker.locked"));
                }
            } else {
                // Failed attempts count ++
                Integer failedAttempts=user.getFailedAttempts();
                if(failedAttempts==null){
                    failedAttempts=0;
                }
                user.setFailedAttempts(++failedAttempts);
                user.setLastSignInAt(user.getCurrentSignInAt());
                user.setCurrentSignInAt(date);
                user.setLastSignInIp(user.getCurrentSignInIp());
                ExWebAuthenticationDetails ew = new ExWebAuthenticationDetails(request);
                user.setCurrentSignInIp(ew.getRemoteAddress());
                Integer signCount=user.getSignInCount();
                if(signCount==null){
                    signCount=0;
                }
                user.setSignInCount(++signCount);
                // Referer
                String referer = request.getHeader(userReferer);
                user.setLastReferer(getReferer(referer));
                authUserServ.add(user);
            }

            int onlyCount = maxFailedAttempts - user.getFailedAttempts();
            return new BadCredentialsException(messages.getMessage(
                    "AccountStatusUserDetailsChecker.onlyCount", new Object[]{onlyCount}));
        }

        throw new BadCredentialsException(messages.getMessage(
                "AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
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


    public void setAuthUserServ(AuthUserServ authUserServ) {
        this.authUserServ = authUserServ;
    }

    public void setUserReferer(String userReferer) {
        this.userReferer = userReferer;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setMaxFailedAttempts(int maxFailedAttempts) {
        this.maxFailedAttempts = maxFailedAttempts;
    }

    public void setLockedTime(long lockedTime) {
        this.lockedTime = lockedTime;
    }
}
