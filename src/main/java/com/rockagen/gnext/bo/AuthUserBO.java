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

package com.rockagen.gnext.bo;


import com.rockagen.gnext.enums.UserReferer;
import com.rockagen.gnext.enums.UserType;
import com.rockagen.gnext.po.AuthGroup;

import java.util.HashSet;
import java.util.Set;

/**
 * Business object
 * @author ra
 * @since JDK1.8
 */
public class AuthUserBO {

    private String uid;
    private String name;
    private String email;
    private String largeAvatar;
    private String avatar;
    private String phone;
    private String address;
    private String password;
    private UserReferer createUserReferer;
    private UserType type;
    private Set<AuthGroup> groups;


    public Set<AuthGroup> getGroups() {
        return groups;
    }

    public void setGroups(Set<AuthGroup> groups) {
        this.groups = groups;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public UserReferer getCreateUserReferer() {
        return createUserReferer;
    }

    public void setCreateUserReferer(UserReferer createUserReferer) {
        this.createUserReferer = createUserReferer;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
     * @param user {@link AuthUserBO}
     */
    public void addGroupFromUser(AuthUserBO user) {
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
