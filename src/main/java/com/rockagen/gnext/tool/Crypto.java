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
package com.rockagen.gnext.tool;

import com.rockagen.commons.util.CommUtil;
import com.rockagen.commons.util.MDUtil;
import org.apache.commons.codec.digest.HmacUtils;

/**
 * Crypto utils
 * 
 * @author RA
 */
public class Crypto {

	/**
	 * Obtain Hex salt
	 * 
	 * @return 32 bytes salt
	 */
	public static String nextSalt(){
		return CommUtil.generateRandomCode(32).toLowerCase();
	}

	/**
	 * User passwd
	 * <p> hmac(md5(hmac(salt,value)),value)</p>
	 * @param salt slat
	 * @param value value
	 * @return passwd
	 */
	public static String passwd(String salt, String value){
		String a=hmacSha1Hex(salt,value);
		String key=MDUtil.md5Hex(a);
		String result=hmacSha1Hex(key,value);
		return result;
	}	
	
	/**
	 * Check password is valid?
	 * @param encPass - a pre-encoded password
	 * @param rawPass - a raw password to encode and compare against the pre-encoded password
	 * @param salt - a salt value.
	 * @return true if valid
	 */
	public static boolean passwdValid(String encPass, String rawPass,String salt){
		return encPass.equals(passwd(salt,rawPass));
	}

	/**
	 * Hmac with sha256 hex
	 * @param key key
	 * @param value value
	 * @return hmac String
	 */
	public static String hmacSha256Hex(String key,String value){
		checkArguments(key, value);
		return HmacUtils.hmacSha1Hex(key, value);

	}
	
	/**
	 * Hmac with sha1 hex
	 * @param key key
	 * @param value value
	 * @return hmac String
	 */
	public static String hmacSha1Hex(String key,String value){
		checkArguments(key, value);
		return HmacUtils.hmacSha1Hex(key, value);

	}
	
	/**
	 * Default hmac(HmacSha1)
	 * @param key key
	 * @param value value
	 * @return hmac String
	 */
	public static String hmac(String key,String value){
		return hmacSha1Hex(key, value);
	}

	/**
	 * Check encrypt arguments
	 * 
	 * @param arg1 string
	 * @param arg2 string
	 */
	private static void checkArguments(String arg1, String arg2) {
		if (CommUtil.isBlank(arg1))
			throw new IllegalArgumentException(
					"value must not be null or empty.");
		if (CommUtil.isBlank(arg2))
			throw new IllegalArgumentException(
					"value must not be null or empty.");
	}
	
	


}
