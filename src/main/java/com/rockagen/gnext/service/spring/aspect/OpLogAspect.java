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
package com.rockagen.gnext.service.spring.aspect;

import com.rockagen.gnext.annotation.Plog;
import com.rockagen.gnext.po.AuthUser;
import com.rockagen.gnext.po.nosql.OpLog;
import com.rockagen.gnext.service.AuthUserServ;
import com.rockagen.gnext.service.spring.security.extension.ExWebAuthenticationDetails;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * Operate log aspect weaver,using <code>Around</code> Mode
 * 
 * @author RA
 */
public class OpLogAspect {

	// ~ Instance fields ==================================================

	@Resource
	private MongoTemplate mongoTemplate;

	@Resource
	private AuthUserServ authUserServ;

	// ~ Methods ==================================================




	/**
	 * [Main] {@link Around}
	 * @param pjp {@link ProceedingJoinPoint}
	 * @return Object
	 * @throws Throwable
	 */
	public Object around(ProceedingJoinPoint pjp) throws Throwable {

		Object obj = null;

		MethodSignature signature = (MethodSignature) pjp.getSignature();
		Method method = signature.getMethod();
		
		int result=1;
		String message="";
		String action=getPlogValue(method);
		Date startAt=new Date();
		try {
			// call target object method
			obj = pjp.proceed();
		} catch (Exception e) {
			result=0;
			message=e.getMessage();
			throw e;
		} finally {
			register(action,startAt,result,message);
		}
		return obj;
	}


	/**
	 * Register request info
	 * @param action action
	 * @param startAt start at
	 * @param result result 0,1
	 * @param message message
	 */
	private void register(String action,Date startAt,int result,String message){

		Date endAt=new Date();
		OpLog l=new OpLog();


		l.setAction(action);
		l.setStartAt(startAt);
		l.setEndAt(endAt);
		l.setIp(getIp());
		l.setTimes(l.getEndAt().getTime() - l.getStartAt().getTime());
		l.setMessage(message);
		l.setResult(result);
		l.setUsername(getUsername());

		AuthUser au=authUserServ.load(getUsername());
		if(au!=null){
			if(au.getLastUserReferer()!=null){
			l.setUserReferer(au.getLastUserReferer().value());
			}
			if(au.getType()!=null){
				l.setUserType(au.getType().value());
			}
		}
		mongoTemplate.save(l);

	}


	/**
	 * Get {@link Authentication}
	 * @return {@link Authentication}
	 */
	private Authentication getAuthentication(){
		SecurityContext ctx=SecurityContextHolder.getContext();
		if(ctx!=null){
			Authentication auth=ctx.getAuthentication();
			if(auth!=null){
				return auth;
			}
		}
		return null;
	}

	/**
	 * Get username
	 * @return username
	 */
	private String getUsername(){
		String username="unknown";
		Authentication auth=getAuthentication();
		if(auth!=null){
			return auth.getName();
		}
		return username;
	}

	/**
	 * Get ip address
	 * @return ip
	 */
	private String getIp(){
		String ip="0.0.0.0";
		Authentication auth=getAuthentication();
		if(auth!=null){
			return ((ExWebAuthenticationDetails)auth.getDetails()).getRemoteAddress();
		}
		return ip;
	}

	/**
	 * Get {@link com.rockagen.gnext.annotation.Plog} value
	 * 
	 * @param method method
	 * @return {@link com.rockagen.gnext.annotation.Plog} value
	 */
	private String getPlogValue(Method method) {
		if (method.isAnnotationPresent(Plog.class)) {
			Plog plog=method.getAnnotation(Plog.class);
			return plog.value();
		} else {
			return method.getDeclaringClass()+"#"+method.getName();
		}
	}


}
