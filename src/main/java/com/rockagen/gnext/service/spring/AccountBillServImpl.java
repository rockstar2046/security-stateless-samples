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

import com.rockagen.gnext.po.AccountBill;
import com.rockagen.gnext.qo.QueryObject;
import com.rockagen.gnext.service.AccountBillServ;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the {@link AccountBillServ} interface
 * @author ra
 * @since JDK1.8
 */
@Service("accountBillServ")
public class AccountBillServImpl extends QueryObjectGenericServImpl<AccountBill,Long> implements AccountBillServ {

    @Override
    public List<AccountBill> loadListByAccount(long accountId) {
        QueryObject qo=new QueryObject();
        DetachedCriteria dc=qo.generateDetachedCriteria(AccountBill.class);
        dc.createAlias("account","a");
        dc.add(Restrictions.eq("a.id", accountId));
        dc.addOrder(Order.desc("createdAt"));
        return find(qo);
    }
}
