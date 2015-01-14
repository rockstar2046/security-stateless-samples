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
package com.rockagen.gnext.service;

import java.io.Serializable;
import java.util.List;

/**
 * GenericServ Support interface
 * <p>
 * <b>Note: </b>The Generic E is entity object,Q is query object ,PK is primary
 * key
 * </p>
 * 
 * @author RA
 */
public interface GenericServ<E, Q, PK extends Serializable> {

	/**
	 * remove by id
	 * 
	 * @param ids
	 *            primary key
	 */
	@SuppressWarnings("unchecked")
	void remove(final PK... ids);

	/**
	 * Load
	 * 
	 * @param id
	 *            primary key
	 * @return E
	 */
	E load(final PK id);

	/**
	 * find by query object
	 * 
	 * @param queryObject
	 *            query object
	 * @return list
	 */
	List<E> find(final Q queryObject);
	
	
	/**
	 * Find all
	 * @return list
	 */
	List<E> findAll();

	/**
	 * Save
	 * 
	 * @param pojo
	 *            E
	 */
	void add(final E pojo);

}
