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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rockagen.commons.util.CommUtil;
import com.rockagen.gnext.po.KeyValue;
import com.rockagen.gnext.service.KeyValueServ;
import com.rockagen.gnext.tool.Conf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Replace Properties configuration
 * <p>
 * <b>NOTE: Manager by Spring &lt;task:scheduled-tasks&gt;<b>
 * </p>
 * 
 * <p>
 * example:
 * </p>
 * 
 * <pre>
 * &lt;bean id="keyValueMap" class="com.rockagen.gnext.service.spring.KeyValueMap" init-method="init"&gt;
 * 	&lt;property name="keyValueServ" ref="keyValueServ"/&gt;
 * &lt;/bean&gt;
 *   
 * &lt;!-- KEY VALUE MAP TASK --&gt;
 * &lt;task:scheduled-tasks&gt;  
 * 	&lt;task:scheduled ref="keyValueMap" method="update" cron="0 *<code>/</code>5 9-21 * * ?"/&gt;  
 * &lt;/task:scheduled-tasks&gt;
 * </pre>
 * 
 * @author RA
 * @see KeyValue
 */
public class KeyValueMapImpl {

	// ~ Instance fields ==================================================

	private static final Logger log = LoggerFactory.getLogger(KeyValueMapImpl.class);

	private KeyValueServ keyValueServ;

	/**
	 * Key Value Map
	 */
	private final Map<String, String> KEY_VALUE_MAP = new ConcurrentHashMap<String, String>();

	// ~ Methods ==================================================

	/**
	 * Initialize (spring init-method)
	 */
	public void init() {

		List<KeyValue> results = keyValueServ.findAll();
		log.debug("Start create KEY_VALUE_MAP...");
		for (KeyValue result : results) {
			log.debug("Now create : {} => {}",result.getKey(),CommUtil.subPostfix(result.getValue(), 0, 3, "***"));
			KEY_VALUE_MAP.put(result.getKey(), result.getValue());
		}
		log.debug("Done.");
		// Initialize
		Conf.init(getMap());
	}

	/**
	 * Get value
	 * 
	 * @param key
	 * @return
	 */
	public String getValue(String key) {
		if (CommUtil.isBlank(key)) {
			return "";
		}
		return KEY_VALUE_MAP.get(key);
	}

	/**
	 * Get the duplicate map
	 * @return
	 */
	public Map<String, String> getMap() {
		return Collections.unmodifiableMap(KEY_VALUE_MAP);
	}

	/**
	 * Update not persist data
	 */
	public void update() {

		List<KeyValue> results = keyValueServ.findActive();
		log.debug("Start update KEY_VALUE_MAP...");
		for (KeyValue result : results) {
			log.debug("Now update : {} => {}",result.getKey(),CommUtil.subPostfix(result.getValue(), 0, 3, "***"));
			KEY_VALUE_MAP.put(result.getKey(), result.getValue());
		}
		log.debug("Done.");
		// update
		Conf.onChange(getMap());
	}

	/**
	 * @param keyValueServ
	 *            the keyValueServ to set
	 */
	public void setKeyValueServ(KeyValueServ keyValueServ) {
		this.keyValueServ = keyValueServ;
	}

}
