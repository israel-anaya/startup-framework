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

package org.startupframework.dto;

import java.util.Objects;
import java.util.UUID;

import lombok.Data;

/**
 *
 * @author Arq. Jesús Israel Anaya Salazar
 */
@Data
public class DataTransferObjectBase implements DataTransferObject {

	private String id;

	public DataTransferObjectBase() {		
	}

	public DataTransferObjectBase(UUID uuid) {
		this.id = uuid.toString();
	}

	public DataTransferObjectBase(String id) {
		UUID buffer = UUID.fromString(id);
		this.id = buffer.toString();
	}
	
	@Override
	public int hashCode() {
		int hash = 31;
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
		if (!(obj instanceof DataTransferObjectBase)) {
			return false;
		}
		DataTransferObjectBase other = (DataTransferObjectBase) obj;
		return getId().equals(other.getId());
	}

	@Override
	public String toString() {
		return getId();
	}

	@Override
	public void generateId() {
		this.id = UUID.randomUUID().toString();
	}
}
