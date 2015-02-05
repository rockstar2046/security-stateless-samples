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
package com.rockagen.gnext.enums;

/**
 * Error type enum
 * @author ra
 * @since JDK1.8
 */
public enum ErrorType implements IntentState{

    SYS0001("System error."),

    //REG
    REG0001("register error."),
    REG0010("invalid email."),
    REG0011("email has used."),
    REG0020("invalid phone number."),
    REG0021("phone number has used."),
    REG0030("invalid username."),
    REG0031("username has used."),
    REG0040("invalid name."),
    REG0050("invalid address."),
    REG0060("invalid password."),
    
    // USER
    USR0001("user error."),
    USR0010("invalid password."),

    //ACCOUNT
    ACC0001("account error."),
    ACC0010("balance not enough."),
    ACC0011("unlock balance not enough."),
    ACC0020("account has locked."),
    ACC0021("account has expired."),
    ACC0022("account has invalid."),
    
    UNKNOWN("unknown error.");
    /**
     * Value
     */
    private final String val;

    /**
     * Constructor
     *
     * @param val value
     */
    private ErrorType(String val) {
        this.val = val;
    }
    
    @Override
    public String value() {
        return this.val;
    }
}
