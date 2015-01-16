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
 * Authorized roles,provide for spring security.
 * 
 * @author RA
 * @since JPA2.0
 */
@Entity
@Table(name = "AUTH_ROLES")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Pcache
public class AuthRole {

	@Id
	@Column(name = "ID")
	@GeneratedValue
	private Long id;

	@Column(name = "NAME", nullable = false)
	private String name;

	@Column(name = "DESCR")
	private String descr;


	@ManyToMany(cascade = CascadeType.MERGE,fetch = FetchType.LAZY)
	@JoinTable(name = "AUTH_ROLE_RESOURCES",
			joinColumns = @JoinColumn(name = "ROLE_ID"),
			inverseJoinColumns = @JoinColumn(name = "RESOURCE_ID"))
	private Set<AuthResource> resources = new LinkedHashSet<>();

	@ManyToMany(mappedBy = "roles")
	private Set<AuthGroup> groups = new LinkedHashSet<>();

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

	public Set<AuthResource> getResources() {
		return resources;
	}

	public void setResources(Set<AuthResource> resources) {
		this.resources = resources;
	}

	public Set<AuthGroup> getGroups() {
		return groups;
	}

	public void setGroups(Set<AuthGroup> groups) {
		this.groups = groups;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
	

	/**
	 * Add resource  
	 * @param res {@link AuthResource}
	 */
	public void addResource(AuthResource res){
		if(res!=null){
			checkResources();
			resources.add(res);
		}
	}

	/**
	 * Add resource form role
	 * @param role {@link AuthRole}
	 */
	public void addResourcesFromRole(AuthRole role) {
		if(role!=null){
			checkResources();
			resources.addAll(role.getResources());
		}
	}
	
	private void checkResources(){
		if(resources==null){
			resources=new HashSet<>();
		}
	}
}
