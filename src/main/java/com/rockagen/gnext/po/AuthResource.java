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
package com.rockagen.gnext.po;

import com.rockagen.gnext.annotation.Pcache;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Authorized resources,provide for spring security.
 *
 * @author RA
 * @since JPA2.0
 */
@Entity
@Table(name = "AUTH_RESOURCES")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Pcache
public class AuthResource {


    @Id
    @Column(name = "ID")
    @GeneratedValue
    private Long id;

    /**
     * The resource path
     * <p><b>NOTE: Using ANT path mode</b></p>
     */
    @Column(name = "PATH", length = 1024, nullable = false)
    private String path;

    /**
     * The priority. the smaller the value the higher the priority
     */
    @Column(name = "PRIORITY", nullable = false)
    private Integer priority;

    @Column(name = "DESCR")
    private String descr;

    @ManyToMany(mappedBy = "resources", fetch = FetchType.EAGER)
    private Set<AuthRole> roles = new LinkedHashSet<>();

    @Version
    private Long version;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
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
}
