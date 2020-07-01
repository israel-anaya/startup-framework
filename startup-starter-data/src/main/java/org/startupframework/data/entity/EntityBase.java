/*
 * Copyright 2019-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.startupframework.data.entity;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.startupframework.entity.Entity;

import lombok.Data;

/**
 * Base Entity for PersistentObject
 * 
 * For validations view: javax.validation.constraints
 * 
 * https://www.baeldung.com/javax-validation
 * 
 * @author Arq. Jesús Israel Anaya Salazar
 */
@MappedSuperclass
@Data
public abstract class EntityBase implements Entity {

	public static final int ID_LENGTH = 41;
	
	@Id
	@Column(length = ID_LENGTH)
	private String id;

	@Column(nullable = false)
	private Date createdDate;

	@Column(nullable = false)
	private Date modifiedDate;

	@Column(nullable = false)
	private Boolean active = true;

	public EntityBase() {
	}


	@Override
	public int hashCode() {
		int hash = 7;
		hash = 29 * hash + Objects.hashCode(this.getId());
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof EntityBase)) {
			return false;
		}
		EntityBase other = (EntityBase) obj;
		return getId().equals(other.getId());
	}

	@Override
	public String toString() {
		return getId();
	}
}
