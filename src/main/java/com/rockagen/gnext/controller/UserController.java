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

import com.rockagen.gnext.annotation.Plog;
import com.rockagen.gnext.service.AuthUserServ;
import com.rockagen.gnext.vo.UserVO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 *
 * @author RA
 * @since JDK1.8
 */
@RestController
@RequestMapping("/users")
public class UserController extends  ApplicationController{

	@Resource
	private AuthUserServ authUserServ;
	
	@RequestMapping(value="/{id}",method = RequestMethod.GET)
	@Plog("show user info")
	public Object show(@PathVariable String id){
		UserVO b=new UserVO();
		authUserServ.load(id).ifPresent(user -> {
			copy(user, b);
		});
		return b;
	}


}
