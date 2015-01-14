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
package com.rockagen.gnext.test;

import com.rockagen.commons.util.MDUtil;
import com.rockagen.gnext.enums.ErrorType;
import com.rockagen.gnext.enums.UserReferer;
import com.rockagen.gnext.enums.UserType;
import com.rockagen.gnext.exception.SystemException;
import com.rockagen.gnext.po.*;
import com.rockagen.gnext.qo.QueryObject;
import com.rockagen.gnext.service.*;
import com.rockagen.gnext.tool.Crypto;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.security.Key;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/applicationContext.groovy"})
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = false)
@Transactional
public class ServiceTest {

	@Resource
	private KeyValueServ keyValueServ;
	@Resource
	private AuthUserServ authUserServ;
	@Resource
	private AuthGroupServ authGroupServ;
	@Resource
	private AuthResourceServ authResourceServ;
	@Resource
	private AuthRoleServ authRoleServ;


	
	
	
	@Test
	//@Rollback(true)
	public void seed(){
		seedKeyValueService();
		seedAuthorityService();
	}


	/**
	 * Seed key value service
	 */
	private void seedKeyValueService(){
		KeyValue kv=new KeyValue();
		kv.setKey("application.config.filter.parameters");
		kv.setValue("password,phone");
		kv.setActive(1);
		kv.setDescr("Request parameter value to be filtered");
		
		KeyValue skey=new KeyValue();
		skey.setKey("spring.security.token.key");
		skey.setValue("d865542eb3beacad1fa6c5b143db7969");
		skey.setDescr("Spring security authentication hmac key");
		skey.setActive(1);
		
		keyValueServ.add(kv);
		keyValueServ.add(skey);

	}

	/**
	 * Seed authority service
	 */
	private void seedAuthorityService(){

		// Resources
		// all
		AuthResource rootRes=new AuthResource();
		rootRes.setPath("/.*");
		rootRes.setPriority(0);
		rootRes.setDescr("all resource");
		// admin
		AuthResource adminRes=new AuthResource();
		adminRes.setPath("/(?!root)/.*");
		adminRes.setPriority(5);
		adminRes.setDescr("admin resource");
		// message
		AuthResource messageRes=new AuthResource();
		messageRes.setPath("/message/.*");
		messageRes.setPriority(10);
		messageRes.setDescr("message resource");
		// email daily
		AuthResource emailDailyRes=new AuthResource();
		emailDailyRes.setPath("/email/daily/.*");
		emailDailyRes.setPriority(10);
		emailDailyRes.setDescr("email daily resource");
		// email monthly
		AuthResource emailMonthlyRes=new AuthResource();
		emailMonthlyRes.setPath("/email/monthly/.*");
		emailMonthlyRes.setPriority(10);
		emailMonthlyRes.setDescr("email monthly resource");
		// voice
		AuthResource voiceRes=new AuthResource();
		voiceRes.setPath("/voice/.*");
		voiceRes.setPriority(10);
		voiceRes.setDescr("voice resource");
		authResourceServ.add(rootRes);
		authResourceServ.add(adminRes);
		authResourceServ.add(emailDailyRes);
		authResourceServ.add(emailMonthlyRes);
		authResourceServ.add(messageRes);
		authResourceServ.add(voiceRes);
		
		
		// Roles
		// root
		AuthRole rootRole = new AuthRole();
		rootRole.setName("ROLE_ROOT");
		rootRole.setDescr("root privileges");
		Set<AuthResource> rootRess=new HashSet<>();
		rootRess.add(rootRes);
		rootRole.setResources(rootRess);
		// admin
		AuthRole adminRole = new AuthRole();
		adminRole.setName("ROLE_ADMIN");
		adminRole.setDescr("admin privileges");
		Set<AuthResource> adminRess=new HashSet<>();
		adminRess.add(adminRes);
		adminRole.setResources(adminRess);
		// email monthly
		AuthRole emailMonthlyRole = new AuthRole();
		emailMonthlyRole.setName("ROLE_EMAIL_MONTHLY");
		emailMonthlyRole.setDescr("email monthly privileges");
		Set<AuthResource> emailMonthlyRess=new HashSet<>();
		emailMonthlyRess.add(emailMonthlyRes);
		emailMonthlyRole.setResources(emailMonthlyRess);
		// email monthly and daily
		AuthRole emailRole = new AuthRole();
		emailRole.setName("ROLE_EMAIL");
		emailRole.setDescr("email monthly and daily privileges");
		Set<AuthResource> emailRoleRess=new HashSet<>();
		emailRoleRess.add(emailMonthlyRes);
		emailRoleRess.add(emailDailyRes);
		emailRole.setResources(emailRoleRess);
		// message
		AuthRole messageRole = new AuthRole();
		messageRole.setName("ROLE_MESSAGE");
		messageRole.setDescr("message privileges");
		Set<AuthResource> messageRess=new HashSet<>();
		messageRess.add(messageRes);
		messageRole.setResources(messageRess);
		// voice
		AuthRole voiceRole = new AuthRole();
		voiceRole.setName("ROLE_VOICE");
		voiceRole.setDescr("voice privileges");
		Set<AuthResource> voiceRess=new HashSet<>();
		voiceRess.add(voiceRes);
		voiceRole.setResources(voiceRess);
		authRoleServ.add(rootRole);
		authRoleServ.add(adminRole);
		authRoleServ.add(emailRole);
		authRoleServ.add(emailMonthlyRole);
		authRoleServ.add(messageRole);
		authRoleServ.add(voiceRole);

		
		// Groups
		// root
		AuthGroup rootGroup=new AuthGroup();
		rootGroup.setName("root");
		rootGroup.setDescr("root group");
		Set<AuthRole> rootRoles=new HashSet<>();
		rootRoles.add(rootRole);
		rootGroup.setRoles(rootRoles);
		// admin
		AuthGroup adminGroup=new AuthGroup();
		adminGroup.setName("adm");
		adminGroup.setDescr("admin group");
		Set<AuthRole> adminRoles=new HashSet<>();
		adminRoles.add(adminRole);
		adminGroup.setRoles(adminRoles);
		// guestv1
		AuthGroup guestV1Group=new AuthGroup();
		guestV1Group.setName("gv1");
		guestV1Group.setDescr("guest v1 level group");
		Set<AuthRole> guestV1Roles=new HashSet<>();
		guestV1Roles.add(emailMonthlyRole);
		guestV1Group.setRoles(guestV1Roles);		
		// guestv2
		AuthGroup guestV2Group=new AuthGroup();
		guestV2Group.setName("gv2");
		guestV2Group.setDescr("guest v2 level group");
		Set<AuthRole> guestV2Roles=new HashSet<>();
		guestV2Roles.add(emailMonthlyRole);
		guestV2Roles.add(emailRole);
		guestV2Group.setRoles(guestV2Roles);
		// guestv3
		AuthGroup guestV3Group=new AuthGroup();
		guestV3Group.setName("gv3");
		guestV3Group.setDescr("guest v3 level group");
		Set<AuthRole> guestV3Roles=new HashSet<>();
		guestV3Roles.add(emailMonthlyRole);
		guestV3Roles.add(emailRole);
		guestV3Roles.add(messageRole);
		guestV3Roles.add(voiceRole);
		guestV3Group.setRoles(guestV3Roles);
		authGroupServ.add(rootGroup);
		authGroupServ.add(adminGroup);
		authGroupServ.add(guestV1Group);
		authGroupServ.add(guestV2Group);
		authGroupServ.add(guestV3Group);

		
		// Users
		// root user
		AuthUser root=buildRootUser();
		Set<AuthGroup> rootGroups=new HashSet<>();
		rootGroups.add(rootGroup);
		root.setGroups(rootGroups);
		// admin user
		AuthUser admin=buildAdminUser();
		Set<AuthGroup> adminGroups=new HashSet<>();
		adminGroups.add(adminGroup);
		admin.setGroups(adminGroups);
		// guest v1 user
		AuthUser guestV1=buildGuestV1();
		Set<AuthGroup> guestV1Groups=new HashSet<>();
		guestV1Groups.add(guestV1Group);
		guestV1.setGroups(guestV1Groups);
		// guest v2 user
		AuthUser guestV2=buildGuestV2();
		Set<AuthGroup> guestV2Groups=new HashSet<>();
		guestV2Groups.add(guestV2Group);
		guestV2.setGroups(guestV2Groups);
		// guest v3 user
		AuthUser guestV3=buildGuestV3();
		Set<AuthGroup> guestV3Groups=new HashSet<>();
		guestV3Groups.add(guestV3Group);
		guestV3.setGroups(guestV3Groups);

		authUserServ.add(root);
		authUserServ.add(admin);
		authUserServ.add(guestV1);
		authUserServ.add(guestV2);
		authUserServ.add(guestV3);
	}

	
	private AuthUser buildRootUser(){
		AuthUser root=new AuthUser();
		root.setName("Root");
		root.setUid("root");
		root.setEmail("root@localhost");
		root.setAddress("USA");
		root.setAvatar(getAvatar(root.getEmail()));
		root.setLargeAvatar(getLargeAvatar(root.getEmail()));
		root.setCreatedAt(new Date());
		root.setCreateReferer(UserReferer.LOCAL);
		root.setEnabled(1);
		root.setType(UserType.ADMIN);
		root.setPassword("root");
		return root;
	}
	
	private AuthUser buildAdminUser() {
		AuthUser admin = new AuthUser();
		admin.setName("Tom");
		admin.setUid("tom");
		admin.setEmail("tom@localhost");
		admin.setAddress("USA");
		admin.setAvatar(getAvatar(admin.getEmail()));
		admin.setLargeAvatar(getLargeAvatar(admin.getEmail()));
		admin.setCreatedAt(new Date());
		admin.setCreateReferer(UserReferer.LOCAL);
		admin.setEnabled(1);
		admin.setType(UserType.ADMIN);
		admin.setPassword("tom");
		return admin;
	}
	
	private AuthUser buildGuestV1(){
		AuthUser guestV1 = new AuthUser();
		guestV1.setName("Guest V1");
		guestV1.setUid("guestv1");
		guestV1.setEmail("guestv1@localhost");
		guestV1.setAddress("USA");
		guestV1.setAvatar(getAvatar(guestV1.getEmail()));
		guestV1.setLargeAvatar(getLargeAvatar(guestV1.getEmail()));
		guestV1.setCreatedAt(new Date());
		guestV1.setCreateReferer(UserReferer.LOCAL);
		guestV1.setEnabled(1);
		guestV1.setType(UserType.GUEST);
		guestV1.setPassword("guestv1");
		return guestV1;
	}
	
	private AuthUser buildGuestV2(){
		AuthUser guestV2 = new AuthUser();
		guestV2.setName("Guest V1");
		guestV2.setUid("guestv2");
		guestV2.setEmail("guestv2@localhost");
		guestV2.setAddress("USA");
		guestV2.setAvatar(getAvatar(guestV2.getEmail()));
		guestV2.setLargeAvatar(getLargeAvatar(guestV2.getEmail()));
		guestV2.setCreatedAt(new Date());
		guestV2.setCreateReferer(UserReferer.LOCAL);
		guestV2.setEnabled(1);
		guestV2.setType(UserType.GUEST);
		guestV2.setPassword("guestv2");
		return guestV2;
	}	
	
	private AuthUser buildGuestV3(){
		AuthUser guestV3 = new AuthUser();
		guestV3.setName("Guest V1");
		guestV3.setUid("guestv3");
		guestV3.setEmail("guestv3@localhost");
		guestV3.setAddress("USA");
		guestV3.setAvatar(getAvatar(guestV3.getEmail()));
		guestV3.setLargeAvatar(getLargeAvatar(guestV3.getEmail()));
		guestV3.setCreatedAt(new Date());
		guestV3.setCreateReferer(UserReferer.LOCAL);
		guestV3.setEnabled(1);
		guestV3.setType(UserType.GUEST);
		guestV3.setPassword("guestv3");
		return guestV3;
	}


	String avatar_base="http://www.gravatar.com/avatar/";
	String avatar_d="d=mm";
	String avatar_s="s=40";
	String avatar_ls="s=460";
	
	private String getAvatar(String email){
		return avatar(email,avatar_s);
	}
	private String getLargeAvatar(String email){
		return avatar(email,avatar_ls);
	}
	
	private String avatar(String email,String sizeMode){
		return avatar_base+MDUtil.md5Hex(email)+"?"+avatar_d+"&"+sizeMode;
	}
	
	
	

	@Test
	@Ignore
	public void testKeyValue(){
		List<KeyValue> kvs=keyValueServ.findAll();
		kvs.stream().forEach(x->{
			System.out.println(x.getKey()+": "+x.getValue());});

	}
}
