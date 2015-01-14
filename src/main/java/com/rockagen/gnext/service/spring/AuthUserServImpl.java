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
package com.rockagen.gnext.service.spring;

import com.rockagen.commons.util.CommUtil;
import com.rockagen.gnext.po.AuthUser;
import com.rockagen.gnext.qo.QueryObject;
import com.rockagen.gnext.service.AuthUserServ;
import com.rockagen.gnext.tool.Crypto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of the <code>AuthUserServ</code> interface
 *
 * @author RA
 */
@Service("authUserServ")
public class AuthUserServImpl extends
        QueryObjectGenericServImpl<AuthUser, Long> implements AuthUserServ {


    //~ Instance fields ==================================================
    private static final Logger log = LoggerFactory.getLogger(AuthUserServImpl.class);

    @Override
    public void passwd(final Long id, final String oldPass, final String newPass) {

        AuthUser po = load(id);
        if (po != null) {
            // Authorized success
            if (Crypto.passwdValid(po.getPassword(),oldPass, po.getSalt())) {
                newPassword(po, newPass);
            } else {
                log.warn("User [{}] old password is invalid,not change.", po.getUid());
            }
        }

    }

    @Override
    public AuthUser load(String account) {
        if (CommUtil.isBlank(account)) {
            return null;
        }
        QueryObject qo = new QueryObject();
        qo.setSql("from AuthUser o where o.uid=:uid");
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("uid", account);
        qo.setMap(map);
        List<AuthUser> list = find(qo);
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }


    private void newPassword(final AuthUser po, final String newPass) {
        String salt = Crypto.nextSalt();
        String passwd = Crypto.passwd(salt, newPass);
        po.setSalt(salt);
        po.setPassword(passwd);
    }

    @Override
    public void add(AuthUser pojo) {
        if (pojo != null) {
            // Check uid
            AuthUser u = load(pojo.getUid());
            if (u == null) {
                String salt = Crypto.nextSalt();
                String passwd = Crypto.passwd(salt, pojo.getPassword());
                pojo.setSalt(salt);
                pojo.setPassword(passwd);
                super.add(pojo);
            }

        }

    }
}
