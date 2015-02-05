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

import com.rockagen.gnext.bo.AuthUserBO;
import com.rockagen.gnext.enums.AccountType;
import com.rockagen.gnext.enums.UserReferer;
import com.rockagen.gnext.enums.UserType;
import com.rockagen.gnext.exception.RegisterException;
import com.rockagen.gnext.po.Account;
import com.rockagen.gnext.po.AuthUser;
import com.rockagen.gnext.service.AccountServ;
import com.rockagen.gnext.service.AuthUserServ;
import com.rockagen.gnext.service.KeyValueServ;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Optional;

import static org.junit.Assert.fail;

/**
 * @author ra
 * @since JDK1.8
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/applicationContext.groovy"})
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = true)
@Transactional
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BaseTest {

    @Resource
    protected KeyValueServ keyValueServ;
    @Resource
    protected AccountServ accountServ;
    @Resource
    protected AuthUserServ authUserServ;
    
    
    protected String uid="test";

    protected Account account;
    protected long accountId;
    protected AuthUser user;

    @Before
    public  void before() throws RegisterException {
        newUser();
        newAccount();

    }

    protected void newAccount() throws RegisterException {
        accountServ.newAccount(uid, AccountType.INDIVIDUAL);

        Optional<Account> acc=accountServ.loadByUid(uid);
        if(acc.isPresent()){
            account=acc.get();
            accountId=account.getId();
        }else{
            fail("account not exist!");
        }
    }

    protected void newUser() throws RegisterException {
        AuthUserBO bo = new AuthUserBO();
        bo.setName("test user");
        bo.setUid("test");
        bo.setEmail("test@localhost.com");
        bo.setPhone("15100000000");
        bo.setAddress("USA");
        bo.setCreateUserReferer(UserReferer.LOCAL);
        bo.setType(UserType.GUEST);
        bo.setPassword("test");
        authUserServ.signup(bo);
        
        Optional<AuthUser> u=authUserServ.load(uid);
        if(u.isPresent()){
            user=u.get();
        }else{
            fail("user not exist!");
        }
    }

    public void refresh(){
        Optional<Account> acc=accountServ.loadByUid(uid);
        acc.ifPresent(x->{
            account = x;
            accountId = account.getId();
        });

        authUserServ.load(uid).ifPresent(x->user=x);
    }
}
