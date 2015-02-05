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
package com.rockagen.gnext.service;


import com.rockagen.gnext.enums.AccountType;
import com.rockagen.gnext.exception.AccountException;
import com.rockagen.gnext.po.Account;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Account Service interface
 * @author ra
 * @since JDK1.8
 */
public interface AccountServ extends QueryObjectGenericServ<Account,Long> {

    /**
     * Load by user account
     *
     * @param uid username or email or phone
     *
     * @return Account
     */
    Optional<Account> loadByUid(String uid);
    
    /**
     * Load by user account
     *
     * @param uid user id
     *
     * @return Account
     */
    Optional<Account> loadByUid(long uid);
    
    /**
     * Load by user account
     *
     * @param uid username or email or phone
     *
     * @return Accounts
     */
    List<Account> loadListByUid(String uid);   
    
    /**
     * Load by user account
     *
     * @param uid user id
     *
     * @return Accounts
     */
    List<Account> loadListByUid(long uid);

    /**
     * Initialize an account
     * @param uid uid
     * @param type type
     * @see AuthUserServ
     */
    void newAccount(String uid, AccountType type);
    
    /**
     * Initialize an account
     * @param uid user id
     * @param type type
     * @see AuthUserServ
     */
    void newAccount(long uid, AccountType type);


    /**
     * Balance increase
     * @param id id
     * @param amount amount
     * @param remark remark
     */
    void increase(long id, BigDecimal amount, String remark);

    /**
     * Balance decrease
     * @param id id
     * @param amount amount
     * @param remark remark
     */
    void decrease(long id, BigDecimal amount, String remark) throws AccountException;
    
    /**
     * Pre take amount
     * <p>/p>
     * @param id id
     * @param amount amount
     * @param remark remark
     */
    void preTakeAmount(long id, BigDecimal amount, String remark) throws AccountException;
    
    /**
     * Take pre amount
     * @param id id
     * @param amount amount
     * @param remark remark
     */
    void takePreAmount(long id, BigDecimal amount, String remark) throws AccountException;

    /**
     * Check balance enough?
     * @param id id
     * @param amount amount
     * @return true if enough
     */
    boolean checkBalance(long id, BigDecimal amount);

    /**
     * Check balance enough?
     * @param uid uid
     * @param amount amount
     * @return true if enough
     */
    boolean checkBalance(String uid, BigDecimal amount);
    
    /**
     * Locked balance
     * @param id id
     * @param amount amount
     */
    void lockedBalance(long id, BigDecimal amount) throws AccountException;
    
    /**
     * Unlocked balance
     * @param id id
     * @param amount amount
     */
    void unLockedBalance(long id, BigDecimal amount) throws AccountException;


    /**
     * Locked account
     * @param id id
     */
    void lockedAccount(long id);   
    
    /**
     * Unlocked account
     * @param id id
     */
    void unLockedAccount(long id);   
    
    /**
     * Expired account
     * @param id id
     */
    void expiredAccount(long id);  
    
    /**
     * Invalid account
     * @param id id
     */
    void invalidAccount(long id);
}
