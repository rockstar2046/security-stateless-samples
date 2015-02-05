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
package com.rockagen.gnext.tool;

import com.rockagen.commons.util.ClassUtil;
import com.rockagen.commons.util.CommUtil;
import com.rockagen.commons.util.MDUtil;
import com.rockagen.commons.util.ReflexUtil;

import java.lang.reflect.Field;

/**
 * Utils
 *
 * @author ra
 * @since JDK1.8
 */
public class Utils {


    /**
     * Gavatar
     */
    public static final String AVATAR_BASE="http://www.gravatar.com/avatar/";
    public static final String AVATAR_D="d=mm";
    public static final String AVATAR_S="s=40";
    public static final String AVATAR_LS="s=460";

    /**
     * Check email
     *
     * @param email email
     * @return true if valid
     */
    public static boolean checkEmail(String email) {
        if(CommUtil.isNotBlank(email)){
            return isEmail(email);
        }
        return true;
    }

    /**
     * Is email?
     * @param email email
     * @return true if valid
     */
    public static boolean isEmail(String email){
        return CommUtil.isEmail(email);
    }

    /**
     * Check uid
     *
     * @param uid username
     * @return true if valid
     */
    public static boolean checkUid(String uid) {
        if (CommUtil.isNotBlank(uid)) {
            String parten = "^[a-zA-Z0-9_.]{3,16}$";
            return uid.matches(parten);
        }
        return false;
    }
    /**
     * Check password
     *
     * @param password password
     * @return true if valid
     */
    public static boolean checkPassword(String password) {
        if (CommUtil.isNotBlank(password)) {
            String parten = "^[a-zA-Z0-9_.~!@#$%^&*()\\-+=]{6,32}$";
            return password.matches(parten);
        }
        return false;
    }
    /**
     * Check phone
     *
     * @param phone phone
     * @return true if valid
     */
    public static boolean checkPhone(String phone) {
        if (CommUtil.isNotBlank(phone)) {
            return isPhoneNum(phone);
        }
        return true;
    }

    /**
     * Is phone?
     * @param phone phone
     * @return true if valid
     */
    public static boolean isPhoneNum(String phone){
        return CommUtil.isPhoneNum(phone);
    }



    /**
     * Check name
     *
     * @param name name
     * @return true if valid
     */
    public static boolean checkName(String name) {
        if (CommUtil.isNotBlank(name)) {
            String parten = "^[a-zA-Z0-9_.\\u4e00-\\u9fa5\\s]{2,16}$";
            return name.matches(parten);
        }
        return true;
    }

    /**
     * Check address
     *
     * @param addr address
     * @return true if valid
     */
    public static boolean checkAddress(String addr) {
        if (CommUtil.isNotBlank(addr)) {
            String parten = "^[a-zA-Z0-9_.\\u4e00-\\u9fa5\\s]{3,32}$";
            return addr.matches(parten);
        }
        return true;
    }


    /**
     * Get avatar addr
     * @param email email
     * @param sizeMode size mode
     * @return avatar addr
     */
    private static String avatar(String email,String sizeMode){
        return AVATAR_BASE+ MDUtil.md5Hex(email)+"?"+AVATAR_D+"&"+sizeMode;
    }

    /**
     * Get avatar address
     * @param email email
     * @return avatar address
     */
    public static String getAvatar(String email){
        return avatar(email,AVATAR_S);
    }

    /**
     * Get large avatar address
     * @param email email
     * @return avatar address
     */
    public static String getLargeAvatar(String email){
        return avatar(email,AVATAR_LS);
    }

    /**
     * Copy src object properties to des object properties
     *
     * @param srcObj camel style object
     * @param desObj snake style object
     */
    public static void copy(Object srcObj, Object desObj) {
        if (srcObj != null && desObj!=null) {
            Field[] fields = ClassUtil.getDeclaredFields(srcObj.getClass(), true);
            for (Field f : fields) {
                String fName = f.getName();
                Object value = ReflexUtil.getFieldValue(srcObj, fName, true);
                ReflexUtil.setFieldValue(desObj, fName, value, true);
            }
        }

    }
}
