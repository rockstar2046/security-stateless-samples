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
package com.rockagen.gnext.po;

import javax.persistence.*;
import java.io.Serializable;


/**
 * Key-Value configuration file
 * @author RA
 * @since JPA2.0
 */
@Entity
@Table(
	name="KEY_VALUES"
)
public class KeyValue implements Serializable{

	@Id
	@Column(name = "ID")
	@GeneratedValue
	private Long id;

	@Column(name = "KEY",unique=true)
	private String key;

	@Column(name = "VALUE",length = 1024)
	private String value;
	/**
	 * Active ?
	 * <ul>
	 * <li>!0 is active</li>
	 * <li>0 is inactive </li>
	 * </ul>
	 */
	@Column(name = "ACTIVE")
	private Integer active;

	@Column(name = "DESCR", length = 512)
	private String descr;
	
	@Version
	private Long version;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getActive() {
		return active;
	}

	/**
	 * Active ?
	 * <ul>
	 * <li>!0 is active</li>
	 * <li>0 is inactive </li>
	 * </ul>
	 */
	public void setActive(Integer active) {
		this.active = active;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
}