/*
 *
 * Copyright (c) 2015, KMKHKTECH Inc. All rights reserved.
 * KMKHKTECH PROPRIETARY/CONFIDENTIAL
 *
 */
package com.rockagen.gnext.tool;

import com.rockagen.commons.util.CommUtil;

/**
 * Utils
 *
 * @author ra
 * @since JDK1.8
 */
public class Utils {


    /**
     * Check email
     *
     * @param email email
     * @return true if valid
     */
    public static boolean checkEmail(String email) {
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
     * Check phone
     *
     * @param phone phone
     * @return true if valid
     */
    public static boolean checkPhone(String phone) {
        if (CommUtil.isNotBlank(phone)) {
            return CommUtil.isPhoneNum(phone);
        }
        return true;
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
}
