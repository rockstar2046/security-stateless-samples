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
package com.rockagen.gnext.tool;

import com.rockagen.commons.util.CommUtil;

/**
 * 
 * Token tools
 * @author ra
 * @since JDK1.8
 */
public class Token {

    /**
     * Get uid from token
     *
     * @param token token
     * @return uid
     */
    public static String getUidFromToken(String token) {

        String[] parts = parsePartAB(token);
        if(parts!=null){
            String partA = CommUtil.decodeBase64(parts[0]);
            return getUidAndExpirationTime(partA)[0];
        }
        return null;
    }


    /**
     * Parse token return partA and partB
     * @param token token
     * @return partA is String[0],partB is String[1]
     */
    public static String[] parsePartAB(String token){
        String[] parts = token.split("\\.");
        if (parts.length == 2 && parts[0].length() > 0 && parts[1].length() > 0) {
            return parts;
        }else{
            return null;
        }

    }


    /**
     * Get uid and expirationTime
     * @param partA part a string
     * @return uid is String[0],expirationTime is String[1]
     */
    public static String[] getUidAndExpirationTime(String partA) {
        String[] parts = partA.split(":");
        String[] uidAndExpirationTime = null;
        if (parts.length == 2) {
            uidAndExpirationTime=new String[2];
            uidAndExpirationTime[0] = parts[1];
            uidAndExpirationTime[1] = parts[0];
        }
        return uidAndExpirationTime;

    }
}
