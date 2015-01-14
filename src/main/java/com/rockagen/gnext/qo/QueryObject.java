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
package com.rockagen.gnext.qo;

import java.util.Arrays;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

/**
 * Query Object
 * <p>
 * Usage:
 * 
 * <blockquote><pre>
 * // example 1:
 * QueryObject q = new QueryObject();
 * q.setSql(&quot;from User where id&lt; ?&quot;);
 * q.setArgs(new Object[] { new Long(100) });
 * q.setIndex(0);
 * q.setSize(20);
 * 
 * // example 2:
 * QueryObject q = new QueryObject();
 * q.setSql(&quot;from User where id&lt;:id&quot;);
 * Map&lt;String, Object&gt; map = new HashMap&lt;String, Object&gt;();
 * map.put(&quot;id&quot;, 10L);
 * q.setIndex(0);
 * q.setSize(20);
 * 
 * // example 3:	
 * QueryObject q = new QueryObject();	
 * DetachedCriteria dc=q.generateDetachedCriteria(User.class);
 * dc.add(Restrictions.lt("id", 10L));
 * ...
 * </pre></blockquote>
 * </p>
 * 
 * @author RA
 */
final public class QueryObject {

	/**
	 * DetachedCriteria
	 */
	private DetachedCriteria detachedCriteria;

	/**
	 * Sql
	 */
	private String sql;

	/**
	 * Sql argument
	 */
	private Object[] args;

	/**
	 * Sql map
	 */
	private Map<String, Object> map;

	/**
	 * First result index
	 */
	private int index = 0;
	/**
	 * Maximum Results
	 */
	private int size = -1;

	public DetachedCriteria getDetachedCriteria() {
		return detachedCriteria;
	}

	/**
	 * Set hibernate {@link DetachedCriteria}
	 * <p>
	 * <b>Note: System will auto generate
	 * {@link DetachedCriteria#forClass(Class)} if detachedCriteria is null</b>
	 * </p>
	 * 
	 * @param detachedCriteria
	 * @see DetachedCriteria
	 * @see Restrictions
	 */
	public void setDetachedCriteria(final DetachedCriteria detachedCriteria) {
		this.detachedCriteria = detachedCriteria;
	}

	/**
	 * Generate hibernate {@link DetachedCriteria}
	 * 
	 * @param clazz
	 * @see DetachedCriteria
	 * @see Restrictions
	 */
	public DetachedCriteria generateDetachedCriteria(final Class<?> clazz) {
		return DetachedCriteria.forClass(clazz);
	}

	/**
	 * Generate hibernate {@link DetachedCriteria}
	 * 
	 * @param clazz
	 * @param alias
	 * @see DetachedCriteria
	 * @see Restrictions
	 */
	public DetachedCriteria generateDetachedCriteria(final Class<?> clazz,
			String alias) {
		return DetachedCriteria.forClass(clazz, alias);
	}

	/**
	 * Generate hibernate {@link DetachedCriteria}
	 * 
	 * @param entityName
	 * @see DetachedCriteria
	 * @see Restrictions
	 */
	public DetachedCriteria generateDetachedCriteria(String entityName) {
		return DetachedCriteria.forEntityName(entityName);
	}

	/**
	 * Generate hibernate {@link DetachedCriteria}
	 * 
	 * @param entityName
	 * @param alias
	 * @see DetachedCriteria
	 * @see Restrictions
	 */
	public DetachedCriteria generateDetachedCriteria(String entityName,
			String alias) {
		return DetachedCriteria.forEntityName(entityName, alias);
	}

	public String getSql() {
		return sql;
	}

	/**
	 * Set sql (<code>PreparedStatement</code>)
	 * 
	 * @param sql
	 */
	public void setSql(String sql) {
		this.sql = sql;
	}

	public Object[] getArgs() {
		if(args!=null){
			return Arrays.copyOf(args, args.length);
		}else {
			return null;
		}
	}

	/**
	 * Set sql (<code>PreparedStatement</code>) arguments
	 * 
	 * @param args
	 */
	public void setArgs(final Object... args) {
		this.args = args;
	}

	public Map<String, Object> getMap() {
		return map;
	}

	/**
	 * Set sql (<code>Map&lt;String,Object&gt;</code>) arguments
	 * <p>
	 * <b>if hql param is ":id",the map key should be "id".</b>
	 * </p>
	 * 
	 * @param map
	 */
	public void setMap(final Map<String, Object> map) {
		this.map = map;
	}

	public int getIndex() {
		return index;
	}

	/**
	 * Set first Result
	 * 
	 * @param index
	 */
	public void setIndex(int index) {

		if (index < 0) {
			index = 0;
		}
		this.index = index;
	}

	public int getSize() {
		return size;
	}

	/**
	 * Set maximum Results
	 * 
	 * @param size
	 */
	public void setSize(int size) {
		if (size < 0) {
			size = -1;
		}
		this.size = size;
	}
}
