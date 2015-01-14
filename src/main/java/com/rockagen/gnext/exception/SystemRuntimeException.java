/*
 * Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rockagen.gnext.exception;

import com.rockagen.gnext.enums.ErrorType;

/**
 * Abstract superclass for all exceptions related to an
 * {@link SystemRuntimeException} object being invalid for whatever reason.
 * *
 * @author ra
 * @since JDK1.8
 */
public abstract class SystemRuntimeException extends RuntimeException{

    /**
     * Constructs an {@code ServiceRuntimeException} with the specified message and root cause.
     *
     * @param msg the {@link com.rockagen.gnext.enums.ErrorType}
     * @param t the root cause
     */
    public SystemRuntimeException(ErrorType msg, Throwable t) {
        
        super(msg.name()+": "+msg.value(), t);
    }

    /**
     * Constructs an {@code ServiceRuntimeException} with the specified message and no root cause.
     *
     * @param msg the {@link com.rockagen.gnext.enums.ErrorType}
     */
    public SystemRuntimeException(ErrorType msg) {
        
        super(msg.name()+": "+msg.value());
    }

    /**
     * Get defined error code
     *
     * @return error code
     */
    public abstract String getErrorCode();
}
