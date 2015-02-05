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

package com.rockagen.gnext.test;

import com.rockagen.gnext.po.AuthGroup;
import com.rockagen.gnext.po.KeyValue;
import com.rockagen.gnext.qo.QueryObject;
import com.rockagen.gnext.service.AuthGroupServ;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.junit.Ignore;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;

public class ServiceTest extends BaseTest{

    @Resource
    private AuthGroupServ authGroupServ;

    @Test
    @Ignore
    public void testKeyValue(){
        List<KeyValue> kvs=keyValueServ.findAll();
        kvs.stream().forEach(x->{
            System.out.println(x.getKey()+": "+x.getValue());});

    }

    @Test
    @Ignore
    public void testXX(){
        QueryObject qo=new QueryObject();
        DetachedCriteria dc=qo.generateDetachedCriteria(AuthGroup.class);
        dc.createAlias("users","u");
        dc.add(Restrictions.eq("u.id", 20L));
        List<AuthGroup> ags=authGroupServ.find(qo);
        ags.forEach(x-> System.err.println(x.getDescr()));
    }
}
