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
import com.rockagen.gnext.po.AuthGroup;
import com.rockagen.gnext.po.AuthUser;
import com.rockagen.gnext.service.AuthUserServ;
import com.rockagen.gnext.tool.Crypto;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.*;


/**
 * An {@link org.springframework.security.authentication.AuthenticationProvider} implementation that retrieves user details from a {@link org.springframework.security.core.userdetails.UserDetailsService}.
 *
 * @author ra
 * @since JDK1.8
 */
public class ExAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {


    // 3 hours
    private long lockedTime = 10800000;

    private AuthUserServ authUserServ;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        UserDetails loadedUser;

        try {
            loadedUser = loadUser(username, authentication.getCredentials().toString());
        } catch (Exception repositoryProblem) {
            throw new InternalAuthenticationServiceException(repositoryProblem.getMessage(), repositoryProblem);
        }

        return loadedUser;
    }


    /**
     * Create a new {@link org.springframework.security.core.userdetails.UserDetails} by uid
     *
     * @param uid         uid
     * @param credentials Credentials(always was password)
     * @return {@link org.springframework.security.core.userdetails.UserDetails}
     * @throws org.springframework.security.authentication.BadCredentialsException if credentials invalid
     */
    private UserDetails loadUser(String uid, String credentials) {

        // Not empty
        if(CommUtil.isBlank(uid) || CommUtil.isBlank(credentials)) {
            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }

        // Load user
        Optional<AuthUser> u=authUserServ.load(uid);

        if(u.filter(x->x.enabled()).isPresent()){
            AuthUser user=u.get();
            // Check credentials
            checkCredentials(user.getPassword(), credentials, user.getSalt());

            // After authenticated handler
            afterAuthenticatedHandler(user);

            List<GrantedAuthority> authorities = new LinkedList<>();
            Set<AuthGroup> groups = user.getGroups();
            if (groups != null && groups.size() > 0) {
                groups.forEach(x -> x.getRoles().forEach(y -> authorities.add(new SimpleGrantedAuthority(y.getName().trim()))));
            }
            return new User(user.getUid(), user.getPassword(), true, true, true, true, authorities);

        }else{
            throw new UsernameNotFoundException(messages.getMessage("",
                    new Object[]{uid}, "User {0} has no GrantedAuthority"));
        }

    }

    /**
     * Check password is valid?
     *
     * @param encPass     - a pre-encoded password
     * @param credentials Credentials(always was password)
     * @param salt        - a salt value.
     * @throws org.springframework.security.authentication.BadCredentialsException if credentials invalid
     */
    protected void checkCredentials(String encPass, String credentials, String salt) {
        if (!Crypto.passwdValid(encPass, credentials, salt)) {
            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }
    }

    /**
     * Do something after authenticated
     *
     * @param user {@link org.newland.kserv.po.AuthUser}
     */
    protected void afterAuthenticatedHandler(AuthUser user) {
        // CASE 1: Auto unlocked
        if (!user.enabled()) {
            Date date = new Date();
            long now = date.getTime();
            if(user.getLastSignInAt()!=null){
                long lastTime = user.getLastSignInAt().getTime();
                if (now - lastTime > lockedTime) {
                    // Unlocked user
                    user.setEnabledAt(date);
                    user.setEnabled(1);
                }
            }
        }
        // CASE 2: ..
    }


    public void setAuthUserServ(AuthUserServ authUserServ) {
        this.authUserServ = authUserServ;
    }

    /**
     * Locked user times
     *
     * @param lockedTime locked time
     */
    public void setLockedTime(long lockedTime) {
        this.lockedTime = lockedTime;
    }
}
