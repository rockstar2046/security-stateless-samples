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
package com.rockagen.gnext.service;

import com.rockagen.gnext.bo.AuthUserBO;
import com.rockagen.gnext.exception.RegisterException;
import com.rockagen.gnext.po.AuthUser;

import java.util.Optional;


/**
 * AuthUser Service interface
 * 
 * @author RA
 */
public interface AuthUserServ  extends QueryObjectGenericServ<AuthUser, Long>{


    /**
     * Change the password
     * @param uid username or email or phone or ca number
     * @param oldPass old password
     * @param newPass new password
     * @return true if success
     */
    boolean passwd(final String uid,final String oldPass,final String newPass);

    /**
     * Authentication
     * @param uid uid
     * @param passwd passwd
     * @return true if authorized
     */
    boolean auth(final String uid,final String passwd);
    
	/**
	 * Load by user account
	 *
	 * @param uid username or email or phone
	 *
	 * @return AuthUser
	 */
	Optional<AuthUser> load(final String uid);


    /**
     * Sign up,uid must not null
     * @param bo bo
     * @throws RegisterException
     */
    void signup(final AuthUserBO bo) throws RegisterException;
	
	
}
