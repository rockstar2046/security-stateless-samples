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

import com.rockagen.gnext.qo.QueryObject;

/**
 * QueryObjectGenericServ Support interface
 * <p>
 * <b>Note: </b>The Generic E is entity object,PK is primary
 * key
 * </p>
 * 
 * @author RA
 * @see GenericServ
 */
public interface QueryObjectGenericServ<E,PK extends Serializable> extends GenericServ<E, QueryObject, PK> {

}
