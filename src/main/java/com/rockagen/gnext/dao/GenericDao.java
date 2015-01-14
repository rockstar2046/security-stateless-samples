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
package com.rockagen.gnext.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * GenericDao Support  interface
 * 
 * <p><b>Note: </b>The Generic E is entity object,PK is primary key</p>
 * @author RA
 */
public interface GenericDao<E,PK extends Serializable> {

	/**
	 * Delete by id
	 * 
	 * @param id primary key
	 */
	void delete(final PK id);

	/**
	 * Delete by pojo
	 * 
	 * @param pojo E
	 */
	void delete(final E pojo);

	/**
	 * Get by id
	 * 
	 * @param id primary key
	 * @return E
	 */
	E get(final PK id);

	/**
	 * Sql Query
	 * @param sql sql
	 * @param start start
	 * @param count count
	 * @param values params
	 * @return list
	 */
	List<?> query(final String sql,final int start,final int count, final Object... values);
	/**
	 * Sql Query
	 * @param sql sql
	 * @param start start
	 * @param count count
	 * @param values params
	 * @return list
	 */
	List<?> query(final String sql,final int start,final int count, final Map<String, Object> values);
	/**
	 * Sql Query
	 * 
	 * @param sql sql
	 * @param values params
	 * @return list
	 */
	List<?> query(final String sql, final Object... values);
	/**
	 * Sql Query
	 * 
	 * @param sql sql
	 * @param values params
	 * @return list
	 */
	List<?> query(final String sql, final Map<String, Object> values);
	
	/**
	 * Save and update
	 * 
	 * @param pojo E
	 */
	void save(final E pojo);

	/**
	 * Execute the update or delete statement.
	 * 
	 * @param sql sql
	 * @return object
	 */
	int executeUpdate(final String sql, final Object... values);


}
