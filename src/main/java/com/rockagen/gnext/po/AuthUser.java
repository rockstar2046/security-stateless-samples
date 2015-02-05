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

import com.rockagen.gnext.enums.UserReferer;
import com.rockagen.gnext.enums.UserType;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Authorized users,provide for spring security.
 *
 * @author RA
 * @since JPA2.0
 */
@Entity
@Table(name = "AUTH_USERS")
public class AuthUser {

    //~ Instance fields ==================================================
    @Id
    @Column(name = "ID")
    @GeneratedValue
    private Long id;

    @Column(name = "UID", nullable = false, unique = true)
    private String uid;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "SALT", nullable = false)
    private String salt;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "EMAIL")
    private String email;
    
    @Column(name = "AVATAR")
    private String avatar;
    
    @Column(name="LARGE_AVATAR")
    private String largeAvatar;
    
    @Column(name = "PHONE")
    private String phone;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "RESET_PASSWORD_TOKEN")
    private String resetPasswordToken;

    @Column(name = "RESET_PASSWORD_SENT_AT")
    private Date resetPasswordSentAt;

    @Column(name = "ENABLED", nullable = false)
    private Integer enabled;

    @Column(name = "ENABLED_AT")
    private Date enabledAt;

    @Column(name = "LOCKED_AT")
    private Date lockedAt;

    @Column(name = "EXPIRED_AT")
    private Date expiredAt;

    @Column(name = "FAILED_ATTEMPTS")
    private Integer failedAttempts;

    @Column(name = "SIGN_IN_COUNT")
    private Integer signInCount;

    @Column(name = "CURRENT_SIGN_IN_AT")
    private Date currentSignInAt;

    @Column(name = "LAST_SIGN_IN_AT")
    private Date lastSignInAt;

    @Column(name = "CREATED_AT")
    private Date createdAt;

    @Column(name = "UPDATED_AT")
    private Date updatedAt;

    @Column(name = "CURRENT_SIGN_IN_IP")
    private String currentSignInIp;

    @Column(name = "LAST_SIGN_IN_IP")
    private String lastSignInIp;

    @Column(name = "AUTHENTICATION_TOKEN")
    private String authenticationToken;

    @Column(name = "CREATE_USER_REFERER")
    @Enumerated(EnumType.STRING)
    private UserReferer createUserReferer;

    @Column(name = "LAST_USER_REFERER")
    @Enumerated(EnumType.STRING)
    private UserReferer lastUserReferer;

    @Column(name = "TYPE")
    private UserType type;

    @ManyToMany(cascade = CascadeType.MERGE,fetch = FetchType.EAGER)
    @JoinTable(name = "AUTH_GROUP_USERS",
            joinColumns = @JoinColumn(name = "GROUP_ID"),
            inverseJoinColumns = @JoinColumn(name = "USER_ID"))
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<AuthGroup> groups = new LinkedHashSet<>();

    @Version
    private Long version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


    public String getLargeAvatar() {
        return largeAvatar;
    }

    public void setLargeAvatar(String largeAvatar) {
        this.largeAvatar = largeAvatar;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getResetPasswordToken() {
        return resetPasswordToken;
    }

    public void setResetPasswordToken(String resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
    }

    public Date getResetPasswordSentAt() {
        return resetPasswordSentAt;
    }

    public void setResetPasswordSentAt(Date resetPasswordSentAt) {
        this.resetPasswordSentAt = resetPasswordSentAt;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
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

    public Integer getFailedAttempts() {
        return failedAttempts;
    }

    public void setFailedAttempts(Integer failedAttempts) {
        this.failedAttempts = failedAttempts;
    }

    public Integer getSignInCount() {
        return signInCount;
    }

    public void setSignInCount(Integer signInCount) {
        this.signInCount = signInCount;
    }

    public Date getCurrentSignInAt() {
        return currentSignInAt;
    }

    public void setCurrentSignInAt(Date currentSignInAt) {
        this.currentSignInAt = currentSignInAt;
    }

    public Date getLastSignInAt() {
        return lastSignInAt;
    }

    public void setLastSignInAt(Date lastSignInAt) {
        this.lastSignInAt = lastSignInAt;
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

    public String getCurrentSignInIp() {
        return currentSignInIp;
    }

    public void setCurrentSignInIp(String currentSignInIp) {
        this.currentSignInIp = currentSignInIp;
    }

    public String getLastSignInIp() {
        return lastSignInIp;
    }

    public void setLastSignInIp(String lastSignInIp) {
        this.lastSignInIp = lastSignInIp;
    }

    public String getAuthenticationToken() {
        return authenticationToken;
    }

    public void setAuthenticationToken(String authenticationToken) {
        this.authenticationToken = authenticationToken;
    }

    public UserReferer getCreateUserReferer() {
        return createUserReferer;
    }

    public void setCreateUserReferer(UserReferer createUserReferer) {
        this.createUserReferer = createUserReferer;
    }

    public UserReferer getLastUserReferer() {
        return lastUserReferer;
    }

    public void setLastUserReferer(UserReferer lastUserReferer) {
        this.lastUserReferer = lastUserReferer;
    }

    public Set<AuthGroup> getGroups() {
        return groups;
    }

    public void setGroups(Set<AuthGroup> groups) {
        this.groups = groups;
    }
    
    public boolean enabled(){
        return (this.enabled !=null && this.enabled >0);
    }

    /**
     * Add group
     * @param group {@link AuthGroup}
     */
    public void addGroup(AuthGroup group){
        if(group!=null){
            checkGroups();
            groups.add(group);
        }
    }

    /**
     * Add group form user
     * @param user {@link AuthUser}
     */
    public void addGroupFromUser(AuthUser user) {
        if(user!=null){
            checkGroups();
            groups.addAll(user.getGroups());
        }
    }

    private void checkGroups(){
        if(groups==null){
            groups=new HashSet<>();
        }
    }
}
