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

import java.util.List;

import org.springframework.stereotype.Service;

import com.rockagen.gnext.po.KeyValue;
import com.rockagen.gnext.qo.QueryObject;
import com.rockagen.gnext.service.KeyValueServ;

/**
 * Implementation of the <code>KeyValueServ</code> interface
 * @author RA
 */
@Service("keyValueServ")
public class KeyValueServImpl extends QueryObjectGenericServImpl<KeyValue,Long> implements KeyValueServ{
	
	@Override
	public List<KeyValue> findActive() {
		QueryObject qo=new QueryObject();
		qo.setSql("from KeyValue o where o.active=0");
		return find(qo);
	}
}
