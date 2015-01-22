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

import com.rockagen.commons.util.CommUtil;
import com.rockagen.gnext.enums.UserReferer;
import com.rockagen.gnext.po.AuthUser;
import com.rockagen.gnext.service.AuthUserServ;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;

/**
 * Authentication Handler
 *
 * @author ra
 * @since JDK1.8
 */
public class ExAuthenticationHandler implements MessageSourceAware {


    protected MessageSourceAccessor messages=SpringSecurityMessageSource.getAccessor();
    /**
     * HttpServletRequest Header params
     */
    private String tokenName = "X-AUTH-TOKEN";

    private String userReferer = "X-Referer";

    private String username = "uid";

    private int maxFailedAttempts = 6;
    // 3 hours
    private long lockedTime = 10800000;

    private AuthUserServ authUserServ;

    private ExTokenAuthentication exTokenAuthentication;


    /**
     * Authentication success handler
     *
     * @param request        request
     * @param response       response
     * @param authentication {@link org.springframework.security.core.Authentication}
     */
    public void successHandler(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        String uid = authentication.getName();
        successRegister(uid, request);
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
     * Success register
     *
     * @param uid     uid
     * @param request request
     */
    private void successRegister(String uid, HttpServletRequest request) {
        String currentIp = new ExWebAuthenticationDetails(request).getRemoteAddress();
        Optional<AuthUser> user = authUserServ.load(uid);
        user.ifPresent(x -> {
                    Date now = new Date();
                    x.setLastSignInAt(x.getCurrentSignInAt());
                    x.setCurrentSignInAt(now);
                    x.setLastSignInIp(x.getCurrentSignInIp());
                    x.setCurrentSignInIp(currentIp);
                    Integer signCount = Optional.ofNullable(x.getSignInCount()).orElse(0);
                    x.setSignInCount(++signCount);
                    x.setFailedAttempts(0);
                    // Referer
                    String referer = request.getHeader(userReferer);
                    x.setLastUserReferer(getReferer(referer));
                    authUserServ.add(x);
                }
        );
    }


    /**
     * Authentication failure handler
     *
     * @param request  request
     * @param response response
     */
    public void failureHandler(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uid = request.getParameter(username);
        try {
            failureRegister(uid, request);
        } catch (AuthenticationException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        }
    }

    /**
     * Failure register
     *
     * @param uid     uid
     * @param request request
     */
    private void failureRegister(String uid, HttpServletRequest request) {
        String currentIp = new ExWebAuthenticationDetails(request).getRemoteAddress();
        Optional<AuthUser> u = authUserServ.load(uid);

        if (u.filter(AuthUser::enabled).isPresent()) {
            AuthUser user = u.get();
            Date date = new Date();

            long now = date.getTime();


            // Failed attempts count ++
            Integer failedAttempts = user.getFailedAttempts();
            if (failedAttempts == null) {
                failedAttempts = 0;
            }
            user.setFailedAttempts(++failedAttempts);
            user.setLastSignInAt(user.getCurrentSignInAt());
            user.setCurrentSignInAt(date);
            user.setLastSignInIp(user.getCurrentSignInIp());
            user.setCurrentSignInIp(currentIp);
            Integer signCount = user.getSignInCount();
            if (signCount == null) {
                signCount = 0;
            }
            user.setSignInCount(++signCount);
            // Referer
            String referer = request.getHeader(userReferer);
            user.setLastUserReferer(getReferer(referer));

            if(user.getLastSignInAt()!=null){
                long lastTime = user.getLastSignInAt().getTime();
                // auto lock
                if (user.getFailedAttempts() >= maxFailedAttempts && (now - lastTime) <= lockedTime) {
                    // Locked user
                    user.setLockedAt(date);
                    user.setEnabled(0);
                }
            }

            authUserServ.add(user);

            // locked?
            if (user.getEnabled() < 1) {
                throw new DisabledException(messages.getMessage("AccountStatusUserDetailsChecker.locked"));
            }

            int onlyCount = maxFailedAttempts - user.getFailedAttempts();
            throw new BadCredentialsException(messages.getMessage(
                    "AccountStatusUserDetailsChecker.onlyCount", new Object[]{onlyCount}));


        } else {
            throw new BadCredentialsException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }

    }

    public UserReferer getReferer(String referer) {
        UserReferer state = UserReferer.UNKNOWN;
        try {
            state = UserReferer.valueOf(referer);
        } catch (Exception e) {
            // DO NOT
        }
        return state;
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

    public void setUsername(String username) {
        this.username = username;
    }

    public void setMaxFailedAttempts(int maxFailedAttempts) {
        this.maxFailedAttempts = maxFailedAttempts;
    }

    public void setLockedTime(long lockedTime) {
        this.lockedTime = lockedTime;
    }

    public void setExTokenAuthentication(ExTokenAuthentication exTokenAuthentication) {
        this.exTokenAuthentication = exTokenAuthentication;
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        messages=new MessageSourceAccessor(messageSource);
    }
}
