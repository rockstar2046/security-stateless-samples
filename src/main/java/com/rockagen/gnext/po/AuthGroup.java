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
package com.rockagen.gnext.po;

import com.rockagen.gnext.annotation.Pcache;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Authorized group,provide for spring security.
 * 
 * @author RA
 * @since JPA2.0
 */
@Entity
@Table(name = "AUTH_GROUPS")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Pcache
public class AuthGroup {

	@Id
	@Column(name = "ID")
	@GeneratedValue
	private Long id;

	@Column(name = "NAME",nullable = false)
	private String name;

	@Column(name = "DESCR")
	private String descr;

	@ManyToMany(mappedBy = "groups")
	private Set<AuthUser> users = new LinkedHashSet<>();

	@ManyToMany(cascade = CascadeType.MERGE,fetch = FetchType.EAGER)
	@JoinTable(name = "AUTH_GROUP_ROLES",
			joinColumns = @JoinColumn(name = "GROUP_ID"),
			inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
	private Set<AuthRole> roles = new LinkedHashSet<>();

	@Version
	private Long version;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public Set<AuthUser> getUsers() {
		return users;
	}

	public void setUsers(Set<AuthUser> users) {
		this.users = users;
	}

	public Set<AuthRole> getRoles() {
		return roles;
	}

	public void setRoles(Set<AuthRole> roles) {
		this.roles = roles;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}


	/**
	 * Add role
	 * @param role {@link AuthRole}
	 */
	public void addRole(AuthRole role){
		if(role!=null){
			checkRoles();
			roles.add(role);
		}
	}

	/**
	 * Add role form group
	 * @param group {@link AuthGroup}
	 */
	public void addRoleFromGroup(AuthGroup group) {
		if(group!=null){
			checkRoles();
			roles.addAll(group.getRoles());
		}
	}

	private void checkRoles(){
		if(roles==null){
			roles=new HashSet<>();
		}
	}
}
