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

import com.rockagen.gnext.bo.AuthUserBO;
import com.rockagen.gnext.enums.AccountType;
import com.rockagen.gnext.enums.UserReferer;
import com.rockagen.gnext.enums.UserType;
import com.rockagen.gnext.exception.RegisterException;
import com.rockagen.gnext.po.AuthGroup;
import com.rockagen.gnext.po.AuthResource;
import com.rockagen.gnext.po.AuthRole;
import com.rockagen.gnext.po.KeyValue;
import com.rockagen.gnext.service.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author ra
 * @since JDK1.8
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/applicationContext.groovy"})
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = true)
@Transactional
public class Seed{

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
    @Resource
    private AccountServ accountServ;





    @Test
    @Rollback(false)
    public void seed() throws RegisterException {
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
    private void seedAuthorityService() throws RegisterException {


        // Resources
        // all
        AuthResource rootRes=new AuthResource();
        rootRes.setPath("/.*");
        rootRes.setPriority(10000);
        rootRes.setDescr("all resource");
        // admin
        AuthResource adminRes=new AuthResource();
        adminRes.setPath("/(?!root/).*");
        adminRes.setPriority(1000);
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

        // email monthly
        AuthRole emailMonthlyRole = new AuthRole();
        emailMonthlyRole.setName("ROLE_EMAIL_MONTHLY");
        emailMonthlyRole.setDescr("email monthly privileges");
        emailMonthlyRole.addResource(emailDailyRes);
        // email monthly and daily
        AuthRole emailRole = new AuthRole();
        emailRole.setName("ROLE_EMAIL");
        emailRole.setDescr("email monthly and daily privileges");
        emailRole.addResource(emailDailyRes);
        emailRole.addResourcesFromRole(emailMonthlyRole);
        // message
        AuthRole messageRole = new AuthRole();
        messageRole.setName("ROLE_MESSAGE");
        messageRole.setDescr("message privileges");
        messageRole.addResource(messageRes);
        // voice
        AuthRole voiceRole = new AuthRole();
        voiceRole.setName("ROLE_VOICE");
        voiceRole.setDescr("voice privileges");
        voiceRole.addResource(voiceRes);
        // admin
        AuthRole adminRole = new AuthRole();
        adminRole.setName("ROLE_ADMIN");
        adminRole.setDescr("admin privileges");
        adminRole.addResourcesFromRole(messageRole);
        adminRole.addResourcesFromRole(emailRole);
        adminRole.addResourcesFromRole(voiceRole);
        adminRole.addResource(adminRes);
        // root
        AuthRole rootRole = new AuthRole();
        rootRole.setName("ROLE_ROOT");
        rootRole.setDescr("root privileges");
        rootRole.addResourcesFromRole(adminRole);
        rootRole.addResource(rootRes);

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
        rootGroup.addRole(rootRole);
        // admin
        AuthGroup adminGroup=new AuthGroup();
        adminGroup.setName("adm");
        adminGroup.setDescr("admin group");
        adminGroup.addRole(adminRole);
        // guestv1
        AuthGroup guestV1Group=new AuthGroup();
        guestV1Group.setName("gv1");
        guestV1Group.setDescr("guest v1 level group");
        guestV1Group.addRole(emailMonthlyRole);
        // guestv2
        AuthGroup guestV2Group=new AuthGroup();
        guestV2Group.setName("gv2");
        guestV2Group.setDescr("guest v2 level group");
        guestV2Group.addRole(emailRole);
        // guestv3
        AuthGroup guestV3Group=new AuthGroup();
        guestV3Group.setName("gv3");
        guestV3Group.setDescr("guest v3 level group");
        guestV3Group.addRole(messageRole);
        guestV3Group.addRole(voiceRole);
        guestV3Group.addRoleFromGroup(guestV2Group);
        authGroupServ.add(rootGroup);
        authGroupServ.add(adminGroup);
        authGroupServ.add(guestV1Group);
        authGroupServ.add(guestV2Group);
        authGroupServ.add(guestV3Group);


        // Users
        // root user
        AuthUserBO root=buildRootUser();
        root.addGroup(rootGroup);
        // admin user
        AuthUserBO admin=buildAdminUser();
        admin.addGroup(adminGroup);
        // guest v1 user
        AuthUserBO guestV1=buildGuestV1();
        guestV1.addGroup(guestV1Group);
        // guest v2 user
        AuthUserBO guestV2=buildGuestV2();
        guestV2.addGroup(guestV2Group);
        // guest v3 user
        AuthUserBO guestV3=buildGuestV3();
        guestV3.addGroup(guestV3Group);

        authUserServ.signup(root);
        authUserServ.signup(admin);
        authUserServ.signup(guestV1);
        authUserServ.signup(guestV2);
        authUserServ.signup(guestV3);
        
        // allocate account 
        accountServ.newAccount(root.getUid(), AccountType.INDIVIDUAL);
        accountServ.newAccount(admin.getUid(), AccountType.INDIVIDUAL);
        accountServ.newAccount(guestV1.getUid(), AccountType.INDIVIDUAL);
        accountServ.newAccount(guestV2.getUid(), AccountType.INDIVIDUAL);
        accountServ.newAccount(guestV3.getUid(), AccountType.INDIVIDUAL);
    }


    private AuthUserBO buildRootUser(){
        AuthUserBO root=new AuthUserBO();
        root.setName("Root");
        root.setUid("root");
        root.setEmail("root@localhost.com");
        root.setPhone("13900000000");
        root.setAddress("USA");
        root.setCreateUserReferer(UserReferer.LOCAL);
        root.setType(UserType.ADMIN);
        root.setPassword("root");
        return root;
    }

    private AuthUserBO buildAdminUser() {
        AuthUserBO admin = new AuthUserBO();
        admin.setName("Tom");
        admin.setUid("tom");
        admin.setEmail("tom@localhost.com");
        admin.setPhone("13800000000");
        admin.setAddress("USA");
        admin.setCreateUserReferer(UserReferer.LOCAL);
        admin.setType(UserType.ADMIN);
        admin.setPassword("tom");
        return admin;
    }

    private AuthUserBO buildGuestV1(){
        AuthUserBO guestV1 = new AuthUserBO();
        guestV1.setName("Guest V1");
        guestV1.setUid("guestv1");
        guestV1.setEmail("guestv1@localhost.com");
        guestV1.setPhone("13800000001");
        guestV1.setAddress("USA");
        guestV1.setCreateUserReferer(UserReferer.LOCAL);
        guestV1.setType(UserType.GUEST);
        guestV1.setPassword("guestv1");
        return guestV1;
    }

    private AuthUserBO buildGuestV2(){
        AuthUserBO guestV2 = new AuthUserBO();
        guestV2.setName("Guest V2");
        guestV2.setUid("guestv2");
        guestV2.setEmail("guestv2@localhost.com");
        guestV2.setPhone("13800000002");
        guestV2.setAddress("USA");
        guestV2.setCreateUserReferer(UserReferer.LOCAL);
        guestV2.setType(UserType.GUEST);
        guestV2.setPassword("guestv2");
        return guestV2;
    }

    private AuthUserBO buildGuestV3(){
        AuthUserBO guestV3 = new AuthUserBO();
        guestV3.setName("Guest V3");
        guestV3.setUid("guestv3");
        guestV3.setEmail("guestv3@localhost.com");
        guestV3.setPhone("13800000003");
        guestV3.setAddress("USA");
        guestV3.setCreateUserReferer(UserReferer.LOCAL);
        guestV3.setType(UserType.GUEST);
        guestV3.setPassword("guestv3");
        return guestV3;
    }
}
