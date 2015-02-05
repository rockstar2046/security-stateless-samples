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
package com.rockagen.gnext.controller;

import com.rockagen.commons.util.ClassUtil;
import com.rockagen.commons.util.CommUtil;
import com.rockagen.commons.util.ReflexUtil;
import com.rockagen.gnext.tool.Token;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;

/**
 * 
 * Application Controller(Top level)
 * @author ra
 * @since JDK1.8
 */
public class ApplicationController {

    private static final String AUTH_HEADER_NAME = "X-AUTH-TOKEN";

    /**
     * Copy camel style object properties to snake style object properties
     *
     * @param camelObj camel style object
     * @param snakeObj snake style object
     */
    public void copy(Object camelObj, Object snakeObj) {
        if (snakeObj != null && camelObj!=null) {
            Field[] fields = ClassUtil.getDeclaredFields(snakeObj.getClass(), true);
            for (Field f : fields) {
                String f_name = f.getName();
                String fName = CommUtil.snake2camel(f_name);
                Object value = ReflexUtil.getFieldValue(camelObj, fName, true);
                ReflexUtil.setFieldValue(snakeObj, f_name, value, true);
            }
        }

    }

    /**
     * Copy snake style object properties to camel style object properties
     *
     * @param snakeObj snake style object
     * @param camelObj camel style object
     */
    public void copy1(Object snakeObj, Object camelObj) {
        if (snakeObj != null && camelObj != null) {
            Field[] fields = ClassUtil.getDeclaredFields(snakeObj.getClass(), true);
            for (Field f : fields) {
                String f_name = f.getName();
                String fName = CommUtil.snake2camel(f_name);
                Object value = ReflexUtil.getFieldValue(snakeObj, f_name, true);
                ReflexUtil.setFieldValue(camelObj, fName, value, true);
            }
        }

    }


    /**
     * Check errors
     *
     * @param errors
     * @return
     */
    public boolean hasError(Errors errors) {
        if (errors != null && errors.hasErrors()) {
            return true;
        }
        return false;
    }

    /**
     * Handle errors
     *
     * @param errors
     * @return
     */
    public ErrorMsg error(Errors errors) {
        FieldError e = errors.getFieldError();
        ErrorMsg em = new ErrorMsg();
        em.field = e.getField();
        em.message = e.getDefaultMessage();
        em.value = e.getRejectedValue();
        return em;

    }

    /**
     * Get uid
     *
     * @return uid
     */
    public String uid(HttpServletRequest request) {
        String token = request.getHeader(AUTH_HEADER_NAME);
        return Token.getUidFromToken(token);
    }


    protected static class ErrorMsg {
        String message;
        String field;
        Object value;
    }
    
}
