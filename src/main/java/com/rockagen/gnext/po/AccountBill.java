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


import com.rockagen.gnext.enums.AccountOperationType;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Account bill
 * @author ra
 * @since JDK1.8
 */
@Entity
@Table(name = "ACCOUNT_BILLS")
public class AccountBill {

    @Id
    @Column(name = "ID")
    @GeneratedValue
    private Long id;

    @Column(name = "SN",nullable = false)
    private String sn;

    @Enumerated(EnumType.STRING)
    @Column(name = "OPERATION_TYPE",nullable=false)
    private AccountOperationType operationType;
    
    @Column(name = "CREATED_AT",nullable = false)
    private Date createdAt;

    @Column(name = "AMOUNT",precision=16,scale = 8,nullable = false)
    private BigDecimal amount;

    @Column(name = "BEFORE_BALANCE",precision=16,scale = 8,nullable = false)
    private BigDecimal beforeBalance;   
    
    @Column(name = "AFTER_BALANCE",precision=16,scale = 8,nullable = false)
    private BigDecimal afterBalance;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "ACCOUNT_ID",nullable = false)
    private Account account;

    @Column(name = "REMARK")
    private String remark;

    @Version
    private Long version;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public AccountOperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(AccountOperationType operationType) {
        this.operationType = operationType;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getBeforeBalance() {
        return beforeBalance;
    }

    public void setBeforeBalance(BigDecimal beforeBalance) {
        this.beforeBalance = beforeBalance;
    }

    public BigDecimal getAfterBalance() {
        return afterBalance;
    }

    public void setAfterBalance(BigDecimal afterBalance) {
        this.afterBalance = afterBalance;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

