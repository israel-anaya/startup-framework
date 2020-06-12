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

import java.util.ArrayList;
import java.util.List;

import org.startupframework.data.entity.DataConverter;
import org.startupframework.data.entity.Entity;
import org.startupframework.data.service.ChildEntityService;
import org.startupframework.datasource.ChildDataSource;
import org.startupframework.dto.ChildDataTransferObject;

/**
*
* @author Arq. Jesús Israel Anaya Salazar
*/
public abstract class ChildEntityServiceDataSource<DTO extends ChildDataTransferObject, E extends Entity, S extends ChildEntityService<E>>
		extends EntityServiceDataSourceBase<DTO, E, S> implements ChildDataSource<DTO> {

	protected ChildEntityServiceDataSource(final S service, DataConverter<DTO, E> dataConverter) {
		super(service, dataConverter);
	}

	@Override
	public DTO save(String parentId, String childId, DTO dto) {
		E entity = toEntity(dto);
		E result = getService().save(parentId, childId, entity);
		return toDataTransferObject(result);
	}

	@Override
	public DTO findById(String parentId, String childId) {
		E foundItem = getService().findById(parentId, childId);
		return toDataTransferObject(foundItem);
	}

	@Override
	public List<DTO> findAll(String parentId) {
		ArrayList<DTO> data = new ArrayList<>();

		List<E> entities = getService().findAll(parentId);
		for (E entity : entities) {
			data.add(toDataTransferObject(entity));
		}
		return data;
	}

	@Override
	public void deleteById(String parentId, String childId) {
		getService().deleteById(parentId, childId);
	}



}
