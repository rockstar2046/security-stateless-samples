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

import com.rockagen.commons.util.CommUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Configuration map
 * <p>example: <code> Conf.get(xxx);</code> or <code>Conf.xxx</code></p>
 * @author RA
 * 
 */
public class Conf {

	// ~ Constructors ==================================================

	/**
	 */
	private Conf() {

	}

	/**
	 * User Level
	 */
	private final static Map<String, String> MAP = new ConcurrentHashMap<>();

	// ~ Methods ==================================================

	/**
	 * Initialize persistence value
	 * 
	 * <p>
	 * 
	 * <pre>
	 * WARNING: 
	 * 
	 *  May be occurs a problem, if you call this method.
	 *  
	 *  For safety, in unwitting, the best way that not to call this method.
	 * 
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @param map map
	 */
	public static void init(Map<String, String> map) {
		// Initialize map
		MAP.putAll(map);

		// TODO Do some Initialize
	}

	/**
	 * If changed,do something...
	 * 
	 * <p>
	 * 
	 * <pre>
	 * WARNING: 
	 * 
	 *  May be  occurs a problem, if you call this method.
	 *  
	 *  For safety, in unwitting, the best way that not to call this method.
	 * 
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @param map map
	 */
	public static void onChange(Map<String, String> map) {

		// Update map
		MAP.putAll(map);

		// TODO Do some change

	}

	/**
	 * Get value
	 * 
	 * @param key key
	 * @return String value
	 */
	public static String get(String key) {
		if (CommUtil.isBlank(key)) {
			return "";
		}
		return MAP.get(key);
	}

}
