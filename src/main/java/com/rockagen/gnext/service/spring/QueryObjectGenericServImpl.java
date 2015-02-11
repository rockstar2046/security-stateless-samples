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

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.rockagen.commons.util.CommUtil;
import com.rockagen.commons.util.ReflexUtil;
import com.rockagen.gnext.dao.GenericDao;
import com.rockagen.gnext.dao.Hibernate4GenericDao;
import com.rockagen.gnext.dao.hb.Hibernate4GenericDaoImpl;
import com.rockagen.gnext.qo.QueryObject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;

/**
 * Implementation of the <code>ObjectQueryGenericServ</code> interface
 *
 * @author RA
 * @see GenericServImpl
 */
public abstract class QueryObjectGenericServImpl<E, PK extends Serializable>
        extends GenericServImpl<E, QueryObject, PK> {

    @Resource
    private SessionFactory sessionFactory;

    @Override
    public List<?> query(final QueryObject queryObject) {

        if (queryObject == null) {
            return Collections.emptyList();
        }
        Boolean limit = false;
        if (queryObject.getSize() > 0) {
            limit = true;
        }

        if (!CommUtil.isBlank(queryObject.getSql())) {
            if (limit) {
                if (notEmpty(queryObject.getMap())) {
                    return getGenericDao().query(
                            queryObject.getSql(), queryObject.getIndex(),
                            queryObject.getSize(), queryObject.getMap());
                } else {
                    return getGenericDao().query(
                            queryObject.getSql(), queryObject.getIndex(),
                            queryObject.getSize(), queryObject.getArgs());
                }

            } else {
                if (notEmpty(queryObject.getMap())) {
                    return getGenericDao().query(
                            queryObject.getSql(), queryObject.getMap());
                } else {
                    return getGenericDao().query(
                            queryObject.getSql(), queryObject.getArgs());
                }

            }
        } else if (queryObject.getDetachedCriteria() != null) {
            if (limit) {
                return getHibernate4GenericDao().queryByCriteria(
                        queryObject.getDetachedCriteria(),
                        queryObject.getIndex(), queryObject.getSize());
            } else {
                return getHibernate4GenericDao().queryByCriteria(
                        queryObject.getDetachedCriteria());
            }
        } else {
            // XXX
            return Collections.emptyList();
        }

    }

    private boolean notEmpty(Map e) {
        return e != null && !e.isEmpty();
    }

    @Override
    public GenericDao<E, PK> getGenericDao() {
        return getHibernate4GenericDao();
    }


    private Hibernate4GenericDao<E, PK> getHibernate4GenericDao() {
        return new Hibernate4GenericDaoImpl<E, PK>() {

            @Override
            protected Class<E> getEntityClass() {
                return obtainEntityClass();
            }

            @Override
            protected Session getSession() {
                return obtainSession();
            }
        };
    }

    @SuppressWarnings("unchecked")
    protected Class<E> obtainEntityClass() {
        return (Class<E>) ReflexUtil.getSuperClassGenricClass(getClass(), 0);
    }

    /**
     * Get Session
     *
     * @return session
     */
    private Session obtainSession() {
        if (sessionFactory == null) {
            throw new DataSourceLookupFailureException("sessionFactory must not be empty or null");
        }
        return sessionFactory.getCurrentSession();

    }


}
