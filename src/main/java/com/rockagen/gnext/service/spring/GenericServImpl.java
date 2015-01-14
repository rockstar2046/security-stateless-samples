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
package com.rockagen.gnext.service.spring;

import java.io.Serializable;
import java.util.List;

import com.rockagen.gnext.dao.GenericDao;
import com.rockagen.gnext.service.GenericServ;

/**
 * Implementation of the <code>GenericServ</code> interface
 * 
 * @author RA
 */
public abstract class GenericServImpl<E, Q, PK extends Serializable> implements
		GenericServ<E, Q, PK> {

	protected abstract GenericDao<E, PK> getGenericDao();

	@SuppressWarnings("unchecked")
	@Override
	public void remove(PK... ids) {

		for (PK id : ids) {
			getGenericDao().delete(id);
		}
	}

	@Override
	public E load(PK id) {
		return getGenericDao().get(id);
	}

	@Override
	public abstract List<E> find(Q queryObject);
	

	@SuppressWarnings("unchecked")
	@Override
	public List<E> findAll() {
		return (List<E>) getGenericDao().query("");
	}
	
	@Override
	public void add(E pojo) {
		getGenericDao().save(pojo);
	}

}
