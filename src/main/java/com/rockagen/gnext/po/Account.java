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
package com.rockagen.gnext.po;


import com.rockagen.gnext.enums.AccountStatus;
import com.rockagen.gnext.enums.AccountType;
import com.rockagen.gnext.enums.ErrorType;
import com.rockagen.gnext.exception.AccountException;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

/**
 * Account
 * @author ra
 * @since JDK1.8
 */
@Entity
@Table(name = "ACCOUNTS")
public class Account {

    @Id
    @Column(name = "ID")
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS",nullable=false)
    private AccountStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", nullable = false)
    private AccountType type;
    
    /**
     * The priority. the smaller the value the higher the priority
     */
    @Column(name = "PRIORITY")
    private Integer priority;
    
    @Column(name = "ENABLED_AT")
    private Date enabledAt;

    @Column(name = "LOCKED_AT")
    private Date lockedAt;

    @Column(name = "EXPIRED_AT")
    private Date expiredAt;
    
    @Column(name = "INVALID_AT")
    private Date invalidAt;

    @Column(name = "CREATED_AT",nullable = false)
    private Date createdAt;

    @Column(name = "UPDATED_AT")
    private Date updatedAt;

    @Column(name = "BALANCE",precision=16,scale = 8)
    private BigDecimal balance;

    @Column(name = "AVAILABLE_BALANCE",precision=16,scale = 8)
    private BigDecimal availableBalance;  
    
    @Column(name = "LOCKED_BALANCE",precision=16,scale = 8)
    private BigDecimal lockedBalance;

    @Column(name = "REMARK")
    private String remark;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "USER_ID",nullable = false)
    private AuthUser user;

    @Version
    private Long version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public Date getEnabledAt() {
        return enabledAt;
    }

    public void setEnabledAt(Date enabledAt) {
        this.enabledAt = enabledAt;
    }

    public Date getLockedAt() {
        return lockedAt;
    }

    public void setLockedAt(Date lockedAt) {
        this.lockedAt = lockedAt;
    }

    public Date getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(Date expiredAt) {
        this.expiredAt = expiredAt;
    }

    public Date getInvalidAt() {
        return invalidAt;
    }

    public void setInvalidAt(Date invalidAt) {
        this.invalidAt = invalidAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(BigDecimal availableBalance) {
        this.availableBalance = availableBalance;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public AuthUser getUser() {
        return user;
    }

    public void setUser(AuthUser user) {
        this.user = user;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public BigDecimal getLockedBalance() {
        return lockedBalance;
    }

    public void setLockedBalance(BigDecimal lockedBalance) {
        this.lockedBalance = lockedBalance;
    }

    /**
     * Add balance
     * @param amount amount
     */
    public void addBalance(BigDecimal amount){
        if(amount!=null){
            balanceCheck();
            balance=balance.add(amount.abs());
            balanceChange();
        }

    }

    /**
     * Sub balance
     * @param amount amount
     */
    public void subtractBalance(BigDecimal amount) throws AccountException {
        if(amount!=null){
            if(status!=AccountStatus.OPEN){
                throw new AccountException(genErrorTypeByStatus(status));
            }
            balanceCheck();
            BigDecimal tmp=availableBalance.subtract(amount.abs());
            // 余额不足
            if((tmp.compareTo(BigDecimal.ZERO))<0){
                throw  new AccountException(ErrorType.ACC0010);
            }
            balance=balance.subtract(amount.abs());
            balanceChange();
        }
    }



    /**
     * Locked balance
     * @param amount amount
     */
    public void lockedBalance(BigDecimal amount) throws AccountException {
        if(amount!=null){
            balanceCheck();
            BigDecimal tmp=availableBalance.subtract(amount.abs());
            // Balance not enough
            if((tmp.compareTo(BigDecimal.ZERO))<0){
                throw  new AccountException(ErrorType.ACC0010);
            }
            lockedBalance=lockedBalance.add(amount.abs());
            balanceChange();
        }
    }   
    
    /**
     * Unlocked balance
     * @param amount amount
     */
    public void unLockedBalance(BigDecimal amount) throws AccountException {
        if(amount!=null){
            balanceCheck();
            BigDecimal tmp=lockedBalance.subtract(amount.abs());
            // Balance not enough
            if((tmp.compareTo(BigDecimal.ZERO))<0){
                throw  new AccountException(ErrorType.ACC0011);
            }
            lockedBalance=lockedBalance.subtract(amount.abs());
            balanceChange();
        }
    }
    

    private void balanceCheck(){
        if(balance==null){
           initAccount();
        }
    }


    private void initAccount(){
        balance=BigDecimal.ZERO;
        availableBalance=BigDecimal.ZERO;
        lockedBalance=BigDecimal.ZERO;
        priority=20;
    }

    /**
     * Initialize
     */
    public void init(){
        Date date=new Date();
        createdAt=date;
        status=AccountStatus.OPEN;
        initAccount();
    }
    /**
     * Balance change notice
     */
    private void balanceChange(){
        availableBalance=balance.subtract(lockedBalance);
        updatedAt=new Date();
    }

    private ErrorType genErrorTypeByStatus(AccountStatus status) {
        ErrorType et;
        switch (Optional.ofNullable(status).orElse(AccountStatus.INVALID)) {
            case LOCKED:
                et = ErrorType.ACC0020;
                break;
            case EXPIRED:
                et = ErrorType.ACC0021;
                break;
            case INVALID:
                et = ErrorType.ACC0022;
                break;
            default:
                et=ErrorType.ACC0001;
        }
        return et;
    }
    
    
}
