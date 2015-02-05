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

import com.rockagen.commons.util.CommUtil;
import com.rockagen.gnext.enums.AccountOperationType;
import com.rockagen.gnext.enums.AccountStatus;
import com.rockagen.gnext.enums.AccountType;
import com.rockagen.gnext.exception.AccountException;
import com.rockagen.gnext.po.Account;
import com.rockagen.gnext.po.AccountBill;
import com.rockagen.gnext.po.AuthUser;
import com.rockagen.gnext.qo.QueryObject;
import com.rockagen.gnext.service.AccountBillServ;
import com.rockagen.gnext.service.AccountServ;
import com.rockagen.gnext.service.AuthUserServ;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the {@link AccountServ} interface
 *
 * @author ra
 * @since JDK1.8
 */
@Service("accountServ")
public class AccountServImpl extends QueryObjectGenericServImpl<Account, Long> implements AccountServ {

    @Resource
    private AccountBillServ accountBillServ;
    @Resource
    private AuthUserServ authUserServ;

    @Override
    public Optional<Account> loadByUid(long uid) {
        List<Account> list = loadListByUid(uid);
        return filter(list);
    }

    /**
     * Offer the maximum priority of the list.
     *
     * @param list list
     * @return Account
     */
    private Optional<Account> filter(List<Account> list) {
        Account tmp = list.stream().filter(x -> x.getStatus() == AccountStatus.OPEN)
                .min((a, b) -> a.getPriority().compareTo(b.getPriority())).get();
        return Optional.ofNullable(tmp);

    }

    @Override
    public List<Account> loadListByUid(long uid) {
        QueryObject qo = new QueryObject();
        DetachedCriteria dc = qo.generateDetachedCriteria(Account.class);
        dc.createAlias("user", "u");
        dc.add(Restrictions.eq("u.id", uid));
        dc.addOrder(Order.desc("updatedAt"));
        return find(qo);
    }

    @Override
    public void newAccount(long uid, AccountType type) {
        Optional<AuthUser> u = authUserServ.load(uid);
        if (u.isPresent()) {
            Account a = initAccount(u.get(), type);
            add(a);
        }
    }

    @Override
    public Optional<Account> loadByUid(String uid) {
        Optional<AuthUser> u = authUserServ.load(uid);
        if (u.isPresent()) {
            return loadByUid(u.get().getId());
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<Account> loadListByUid(String uid) {
        Optional<AuthUser> u = authUserServ.load(uid);
        if (u.isPresent()) {
            return loadListByUid(u.get().getId());
        }
        return Collections.emptyList();
    }

    @Override
    public void newAccount(String uid, AccountType type) {
        Optional<AuthUser> u = authUserServ.load(uid);
        if (u.isPresent()) {
            newAccount(u.get().getId(), type);
        }
    }

    /**
     * Initialize account*
     *
     * @param user user
     * @param type type
     * @return Account
     */
    private Account initAccount(AuthUser user, AccountType type) {
        Account a = new Account();
        a.init();
        a.setType(Optional.ofNullable(type).orElse(AccountType.INDIVIDUAL));
        a.setUser(user);
        a.setRemark(user.getName());
        return a;
    }

    @Override
    public void increase(long id, BigDecimal amount, String remark) {
        load(id).ifPresent(acc -> {
            // account
            BigDecimal before = acc.getBalance();
            acc.addBalance(amount);
            genAccountBill(acc, AccountOperationType.DEBIT, before, amount, remark);
        });
    }


    @Override
    public void decrease(long id, BigDecimal amount, String remark) throws AccountException {
        Optional<Account> acc = load(id);
        if (acc.isPresent()) {
            // account
            BigDecimal before = acc.get().getBalance();
            acc.get().subtractBalance(amount);
            genAccountBill(acc.get(), AccountOperationType.CREDIT, before, amount, remark);
        }
    }

    @Override
    public void preTakeAmount(long id, BigDecimal amount, String remark) throws AccountException {
        lockedBalance(id, amount);
    }

    @Override
    public void takePreAmount(long id, BigDecimal amount, String remark) throws AccountException {
        unLockedBalance(id, amount);
        decrease(id, amount, remark);
    }

    @Override
    public boolean checkBalance(long id, BigDecimal amount) {
        Optional<Account> account = load(id);
        if (account.isPresent()) {
            return checkBalance(account.get(), amount);
        }
        return false;
    }

    @Override
    public boolean checkBalance(String uid, BigDecimal amount) {
        Optional<Account> account = loadByUid(uid);
        if (account.isPresent()) {
            return checkBalance(account.get(), amount);
        }
        return false;
    }


    @Override
    public void lockedBalance(long id, BigDecimal amount) throws AccountException {
        Optional<Account> acc = load(id);
        if (acc.isPresent()) {
            // account
            acc.get().lockedBalance(amount);
        }
    }

    @Override
    public void unLockedBalance(long id, BigDecimal amount) throws AccountException {
        Optional<Account> acc = load(id);
        if (acc.isPresent()) {
            // account
            acc.get().unLockedBalance(amount);
        }
    }

    @Override
    public void lockedAccount(long id) {
        load(id).ifPresent(acc -> {
            // account
            Date now = new Date();
            acc.setStatus(AccountStatus.LOCKED);
            acc.setLockedAt(now);
            acc.setUpdatedAt(now);
        });
    }

    @Override
    public void unLockedAccount(long id) {
        load(id).ifPresent(acc -> {
            // account
            Date now = new Date();
            acc.setStatus(AccountStatus.OPEN);
            acc.setEnabledAt(now);
            acc.setUpdatedAt(now);
        });

    }

    @Override
    public void expiredAccount(long id) {
        load(id).ifPresent(acc -> {
            // account
            Date now = new Date();
            acc.setStatus(AccountStatus.EXPIRED);
            acc.setExpiredAt(now);
            acc.setUpdatedAt(now);
        });
    }

    @Override
    public void invalidAccount(long id) {
        load(id).ifPresent(acc -> {
            // account
            Date now = new Date();
            acc.setStatus(AccountStatus.INVALID);
            acc.setInvalidAt(now);
            acc.setUpdatedAt(now);
        });
    }


    /**
     * Balance enough?
     *
     * @param account Account
     * @param amount  amount
     * @return true if enough
     */
    private boolean checkBalance(Account account, BigDecimal amount) {
        BigDecimal tmp = account.getAvailableBalance().subtract(amount.abs());
        if ((tmp.compareTo(BigDecimal.ZERO)) < 0) {
            return false;
        }
        return true;

    }

    /**
     * Account bill
     *
     * @param po     po
     * @param amount before balance
     * @param amount amount
     * @param remark remark
     */
    private void genAccountBill(Account po, AccountOperationType opType, BigDecimal beforeBalance, BigDecimal amount, String remark) {
        // account bill
        Date now = new Date();
        AccountBill ab = new AccountBill();
        ab.setRemark(remark);
        ab.setAccount(po);
        ab.setAmount(amount.abs());
        ab.setCreatedAt(now);
        ab.setOperationType(opType);
        ab.setBeforeBalance(beforeBalance);
        ab.setAfterBalance(po.getBalance());
        ab.setSn(getBillSn(po.getUser().getId()));
        accountBillServ.add(ab);

    }


    private String getBillSn(long uid) {
        Date now = new Date();
        String tmp = CommUtil.dateTime2StringFS(now);
        StringBuilder sb = new StringBuilder(tmp);
        String suid = String.format("%08d", uid);
        sb.insert(8, suid);
        return sb.toString();
    }
}
