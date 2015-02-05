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

package com.rockagen.gnext.test;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * User test 
 * @author ra
 * @since JDK1.8
 */
public class AuthUserTest extends BaseTest {
    
    
    @Test
    public void testAuth(){
        boolean r1=authUserServ.auth(uid, "test");
        assertTrue(r1);
    }  
    
    @Test
    public void testPasswd(){
        boolean r1=authUserServ.passwd(uid,"test","test1");
        boolean r2=authUserServ.auth(uid, "test1");
        assertTrue(r1);
        assertTrue(r2);
    }
    
    @Test
    public void testPasswdE(){
        boolean r1=authUserServ.passwd(uid,"test1","test1");
        assertFalse(r1);
    }
    
}
