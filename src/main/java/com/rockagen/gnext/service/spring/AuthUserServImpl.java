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
package com.rockagen.gnext.service.spring;

import com.rockagen.commons.util.CommUtil;
import com.rockagen.gnext.bo.AuthUserBO;
import com.rockagen.gnext.enums.ErrorType;
import com.rockagen.gnext.enums.UserReferer;
import com.rockagen.gnext.enums.UserType;
import com.rockagen.gnext.exception.RegisterException;
import com.rockagen.gnext.po.AuthUser;
import com.rockagen.gnext.qo.QueryObject;
import com.rockagen.gnext.service.AuthUserServ;
import com.rockagen.gnext.tool.Crypto;
import com.rockagen.gnext.tool.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

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
    public boolean passwd(final String uid, final String oldPass, final String newPass) {

        Optional<AuthUser> user = load(uid);
        if (user.isPresent()) {
            AuthUser po = user.get();
            // Authorized success
            if (Crypto.passwdValid(po.getPassword(), oldPass, po.getSalt())) {
                newPassword(po, newPass);
                return true;
            } else {
                log.warn("User [{}] old password is invalid,not change.", po.getUid());
            }
        }
        return false;
    }

    @Override
    public boolean auth(String uid, String passwd) {
        Optional<AuthUser> user = load(uid);
        if(user.isPresent()){
            return Crypto.passwdValid(user.get().getPassword(),passwd,user.get().getSalt());
        }
        return false;
    }
    
    @Override
    public Optional<AuthUser> load(String uid) {
        if (CommUtil.isBlank(uid)) {
            Optional.empty();
        }
        List<AuthUser> list = find(buildQueryObjectFromUid(uid));
        if (list.isEmpty()) {
            return  Optional.empty();
        }
        return Optional.ofNullable(list.get(0));
    }

    private QueryObject buildQueryObjectFromUid(String uid) {

        boolean isEmail = Utils.isEmail(uid);
        boolean isPhone = Utils.isPhoneNum(uid);

        String field = "uid";
        if (isEmail) {
            field = "email";
        } else if (isPhone) {
            field = "phone";
        } else {
            // XXX
        }

        QueryObject qo = new QueryObject();
        qo.setSql("from AuthUser o where o." + field + "=:uid");
        final Map<String, Object> map = new HashMap<>();
        map.put("uid", uid);
        qo.setMap(map);
        return qo;


    }


    private void newPassword(final AuthUser po, final String newPass) {
        String salt = Crypto.nextSalt();
        String passwd = Crypto.passwd(salt, newPass);
        po.setSalt(salt);
        po.setPassword(passwd);
    }


    /**
     * Sign up,phone must not null
     *
     * @param bo bo
     * @throws RegisterException
     */
    public void signup(AuthUserBO bo) throws RegisterException {

        if (bo == null) {
            throw new RegisterException();
        }

        String email = bo.getEmail();
        String phone = bo.getPhone();
        String uid = bo.getUid();
        // email
        if (!Utils.checkEmail(email)) {
            throw new RegisterException(ErrorType.REG0010);
        }
        // phone
        if (!Utils.checkPhone(phone)) {
            throw new RegisterException(ErrorType.REG0020);
        }
        // username
        if (!Utils.checkUid(uid)) {
            throw new RegisterException(ErrorType.REG0030);
        }
        // name
        if (!Utils.checkName(bo.getName())) {
            throw new RegisterException(ErrorType.REG0040);
        }
        // address
        if (!Utils.checkAddress(bo.getAddress())) {
            throw new RegisterException(ErrorType.REG0050);
        }

        if (CommUtil.isNotBlank(email)) {
            // lower case
            email = email.toLowerCase();
            // email exist

            if (load(email).isPresent()) {
                throw new RegisterException(ErrorType.REG0011);
            }
        }

        // uid exist
        if (load(uid).isPresent()) {
            throw new RegisterException(ErrorType.REG0031);
        }
        // phone exist
        if (CommUtil.isNotBlank(uid) && load(phone).isPresent()) {
            throw new RegisterException(ErrorType.REG0021);
        }

        String password = bo.getPassword();
        // all passed
        AuthUser po = new AuthUser();
        Utils.copy(bo, po);


        // if avatar null
        if (po.getLargeAvatar() == null) {
            po.setLargeAvatar(Utils.getLargeAvatar(email));
            po.setAvatar(Utils.getAvatar(email));
        }

        // if user type null
        if (po.getType() == null) {
            po.setType(UserType.GUEST);
        }
        // if user referer null
        if (po.getCreateUserReferer() == null) {
            po.setCreateUserReferer(UserReferer.UNKNOWN);
        }


        Date now = new Date();
        po.setCreatedAt(now);
        po.setEnabled(1);
        po.setEnabledAt(now);
        po.setSignInCount(0);
        String salt = Crypto.nextSalt();
        String passwd = Crypto.passwd(salt, password);
        po.setSalt(salt);
        po.setPassword(passwd);
        super.add(po);
    }

}
