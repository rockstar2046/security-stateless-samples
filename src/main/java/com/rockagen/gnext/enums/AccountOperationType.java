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
 * Account Operation Type
 *
 * @author ra
 * @since JDK1.8
 */
public enum AccountOperationType implements IntentState {
    DEBIT("debit"),
    CREDIT("credit");

    /**
     * Value
     */
    private final String val;

    /**
     * Constructor
     *
     * @param val value
     */
    private AccountOperationType(String val) {
        this.val = val;
    }

    @Override
    public String value() {
        return this.val;
    }
}
