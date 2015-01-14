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
package com.rockagen.gnext.dao.hb;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;

/**
 * The base dao,initialize some hibernate environment
 * 
 * @author RA
 */
public class BaseDao {

	/** The hibernate sessionFactory. */
	private SessionFactory sessionFactory;

	/**
	 * Get session.
	 * 
	 * @return session
	 */
	public Session getSession() {
		return this.sessionFactory.getCurrentSession();
	}

	/**
	 * Sets the hibernate sessionFactory.
	 * 
	 * @param sessionFactory
	 *            hibernate sessionFactory.
	 */
	@Resource
	public void setSessionFactory(SessionFactory sessionFactory) {
		if(sessionFactory==null){
			throw new DataSourceLookupFailureException("sessionFactory must not be empty or null");
		}
		this.sessionFactory = sessionFactory;
	}

}
