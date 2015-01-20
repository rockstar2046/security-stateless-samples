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
import com.rockagen.gnext.po.AuthGroup;
import com.rockagen.gnext.po.AuthUser;
import com.rockagen.gnext.service.AuthUserServ;
import com.rockagen.gnext.tool.Conf;
import com.rockagen.gnext.tool.Crypto;
import com.rockagen.gnext.tool.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Extension token authentication class
 * <p>
 * <pre>
 * token = base64(expirationTime + ":" + uid) + "." + base64(hmac(expirationTime + ":" + uid + ":" + password))
 *
 * password:          That matches the one in the retrieved UserDetails
 * uid:          As identifiable to the UserDetailsService
 * expirationTime:    The date and time when the token expires,expressed in milliseconds
 *     </pre>
 * </p>
 *
 * @author ra
 * @since JDK1.8
 */
public class ExTokenAuthentication {


    private static final Logger log = LoggerFactory.getLogger(ExTokenAuthentication.class);

    // one week
    private long expiredTime = 7 * 24 * 60 * 60 * 1000;

    private String keyName = "spring.security.token.key";

    private AuthUserServ authUserServ;


    /**
     * Validate token
     *
     * @param token token
     * @see #newToken(String)
     * @return true if authenticated
     */
    public boolean authenticated(String token) {
        if (token == null) {
            return false;
        }
        String[] parts = Token.parsePartAB(token);

        if(parts!=null){
            String partA = CommUtil.decodeBase64(parts[0]);
            String partB = CommUtil.decodeBase64(parts[1]);
            String[] userDetails = Token.getUidAndExpirationTime(partA);
            if (userDetails!=null) {
                String uid = userDetails[0];
                String expireTime = userDetails[1];
                long now = System.currentTimeMillis();
                try {
                    // Not expired
                    if (Long.parseLong(expireTime) > now) {
                        AuthUser u = authUserServ.load(uid);
                        if (u != null && u.enabled()) {
                            String passwd = u.getPassword();
                            String vhmac = hmac(uid,expireTime,passwd);
                            return partB.equals(vhmac);
                        }
                    }
                } catch (Exception e) {
                    log.error("Authenticate failed: because {}", e.getMessage());
                }
            }

        }
        return false;

    }

    /**
     * <p>
     * <pre>
     * token = base64(expirationTime + ":" + uid) + "." + base64(hmac(expirationTime + ":" + uid + ":" + password))
     *
     * expirationTime:    The date and time when the token expires,expressed in milliseconds
     * uid:          As identifiable to the UserDetailsService
     * password:          That matches the one in the retrieved UserDetails
     *     </pre>
     * </p>
     *
     * @param uid uid
     * @return new token,null if uid not exist or disabled
     */
    public String newToken(String uid) {
        AuthUser u = authUserServ.load(uid);

        if(u!=null && u.enabled()) {
            String passwd = u.getPassword();
            long expirationTime = System.currentTimeMillis() + expiredTime;

            String partA = expirationTime + ":" + uid;
            String partB = hmac(uid,String.valueOf(expirationTime),passwd);
            String token=CommUtil.encodeBase64(partA) + "." + CommUtil.encodeBase64(partB);
            return token;
        }
        return null;
    }

    /**
     * hmac
     * @param expirationTime expiration time
     * @param uid username
     * @param salt salt
     * @return hmac String
     */
    private String hmac(String uid,String expirationTime,String salt){
        String content=expirationTime + ":" + uid + ":" + salt;
        return Crypto.hmac(Conf.get(keyName), content);

    }

    /**
     * Get {@link UserDetails} from token
     *
     * @param token token
     * @return {@link org.springframework.security.core.userdetails.UserDetails} if token authenticated,otherwise return null
     */
    public UserDetails getUserDetailsFromToken(String token) {
        if (authenticated(token)) {
            // Load user
            AuthUser user = authUserServ.load(Token.getUidFromToken(token));
            if (user != null && user.enabled()) {
                List<GrantedAuthority> authorities = new LinkedList<>();
                Set<AuthGroup> groups = user.getGroups();
                if (groups != null && groups.size() > 0) {
                    groups.forEach(x -> x.getRoles().forEach(y -> authorities.add(new SimpleGrantedAuthority(y.getName().trim()))));
                }
                return new User(user.getUid(), "***", authorities);
            }
        }
        return null;
    }

    public void setExpiredTime(long expiredTime) {
        this.expiredTime = expiredTime;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public void setAuthUserServ(AuthUserServ authUserServ) {
        this.authUserServ = authUserServ;
    }
}
