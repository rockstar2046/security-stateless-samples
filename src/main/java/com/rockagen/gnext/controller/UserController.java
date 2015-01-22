/*
 * Copyright 2014 the original author or authors.
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

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.rockagen.gnext.annotation.Plog;
import com.rockagen.gnext.po.AuthUser;
import com.rockagen.gnext.service.AuthUserServ;
import com.rockagen.gnext.vo.UserVO;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import com.rockagen.commons.util.JsonUtil;
import com.rockagen.gnext.bo.BoUser;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

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
