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

package org.startupframework.data.datasource;

import org.startupframework.data.entity.DataConverter;
import org.startupframework.data.entity.Entity;
import org.startupframework.data.service.EntityService;
import org.startupframework.dto.DataTransferObject;
import org.startupframework.exception.DataException;

import lombok.Getter;

/**
*
* @author Arq. Jesús Israel Anaya Salazar
*/
public abstract class EntityServiceDataSourceBase<DTO extends DataTransferObject, E extends Entity, S extends EntityService<E>> {

	static final String ASSERT_SERVICE = "Should implements service for %s";

	DataConverter<DTO, E> dataConverter;

	@Getter
	final S service;

	protected EntityServiceDataSourceBase(final S service, DataConverter<DTO, E> dataConverter) {
		assert service != null : String.format(ASSERT_SERVICE, this.getClass().getName());
		this.service = service;

		// Class<?>[] arguments =
		// GenericTypeResolver.resolveTypeArguments(this.getClass(),
		// EntityServiceDataSourceBase.class);

		// Class<DTO> dtoType = (Class<DTO>) arguments[0];
		// Class<E> entityType = (Class<E>) arguments[1];

		this.dataConverter = dataConverter;
	}

	protected E toEntity(DTO dto) {
		try {
			return dataConverter.toEntity(dto);
		} catch (Exception ex) {
			throw new DataException(ex);
		}
	}

	protected DTO toDataTransferObject(E entity) {
		try {
			return dataConverter.toDataTransferObject(entity);
		} catch (Exception ex) {
			throw new DataException(ex);
		}
	}

}
