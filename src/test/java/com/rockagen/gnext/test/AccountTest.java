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

import com.rockagen.gnext.enums.AccountStatus;
import com.rockagen.gnext.exception.AccountException;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Account test
 * @author ra
 * @since JDK1.8
 */
public class AccountTest extends BaseTest {

    @Test
    public void testTakeAcccount() throws AccountException {
        BigDecimal take=new BigDecimal("10");
        accountServ.increase(accountId,take,"");
        accountServ.preTakeAmount(accountId, take, "");
        refresh();
        boolean r1=(account.getBalance().compareTo(take)==0);
        assertTrue(r1);
        accountServ.takePreAmount(accountId, take, "");
        refresh();
        boolean r2=(account.getAvailableBalance().compareTo(BigDecimal.ZERO)==0);
        assertTrue(r2);
    }

    @Test
    public void testLockAccount() throws AccountException {
        BigDecimal take=new BigDecimal("10");
        accountServ.increase(accountId,take,"");
        accountServ.lockedBalance(accountId,take);
        refresh();
        boolean r1=(account.getAvailableBalance().compareTo(BigDecimal.ZERO)==0);
        assertTrue(r1);
        accountServ.unLockedBalance(accountId, take);
        refresh();
        boolean r2=(account.getAvailableBalance().compareTo(take)==0);
        assertTrue(r2);
    }
    
    @Test(expected = AccountException.class)
    public void testLockAccountE() throws AccountException {
        BigDecimal take=new BigDecimal("10");
        accountServ.lockedBalance(accountId,take);
    }   
    
    @Test(expected = AccountException.class)
    public void testTakeAcccountE() throws AccountException {
        BigDecimal take=new BigDecimal("10");
        accountServ.preTakeAmount(accountId, take, "");
    }



    @Test
    public void test001CheckBalance(){
        boolean enough=accountServ.checkBalance(accountId, new BigDecimal("10"));
        assertFalse(enough);
    }

    @Test
    public void test002CheckBalance(){
        accountServ.increase(accountId,new BigDecimal("10"),"");
        boolean enough = accountServ.checkBalance(accountId, new BigDecimal("10"));
        assertTrue(enough);
    }
    
    @Test
    public void testExpiredAccount(){
        accountServ.expiredAccount(accountId);
        refresh();
        boolean ret=account.getStatus()== AccountStatus.EXPIRED;
        assertTrue(ret);
    }
    @Test
    public void testLockedAccount(){
        accountServ.lockedAccount(accountId);
        refresh();
        boolean ret=account.getStatus()== AccountStatus.LOCKED;
        assertTrue(ret);
    }
    @Test
    public void testInvalidAccount(){
        accountServ.invalidAccount(accountId);
        refresh();
        boolean ret=account.getStatus()== AccountStatus.INVALID;
        assertTrue(ret);
    }
}
