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
package com.rockagen.gnext.controller;

import com.rockagen.commons.util.ClassUtil;
import com.rockagen.commons.util.CommUtil;
import com.rockagen.commons.util.ReflexUtil;
import com.rockagen.gnext.tool.Token;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.regex.Pattern;

/**
 * 
 * Application Controller(Top level)
 * @author ra
 * @since JDK1.8
 */
public class ApplicationController {

    @Autowired
    protected HttpServletRequest request;
    @Autowired
    protected HttpServletResponse response;

    private static final String AUTH_HEADER_NAME = "X-AUTH-TOKEN";

    /**
     * Copy camel style object properties to snake style object properties
     * @param src
     * @param desc
     */
    public void copy(Object src,Object desc){
        if(desc!=null){
            Field[] fields= ClassUtil.getDeclaredFields(desc.getClass(),true);
            for(Field f:fields){
                String f_name=f.getName();
                String fName= CommUtil.snake2camel(f_name);
                Object value=ReflexUtil.getFieldValue(src, fName, true);
                ReflexUtil.setFieldValue(desc, f_name, value, true);
            }
        }

    }

    /**
     * Get uid
     * @return uid
     */
    public String uid(){
        String token=request.getHeader(AUTH_HEADER_NAME);
        return Token.getUidFromToken(token);
    }
    
}
