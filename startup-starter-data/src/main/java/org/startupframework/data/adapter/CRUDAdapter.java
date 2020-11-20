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

package org.startupframework.data.adapter;

import java.util.ArrayList;
import java.util.List;

import org.startupframework.data.entity.DataConverter;
import org.startupframework.data.service.EntityService;
import org.startupframework.dto.EntityDTO;
import org.startupframework.entity.Entity;
import org.startupframework.exception.DataNotFoundException;
import org.startupframework.service.CRUDService;

/**
 *
 * @author Arq. Jesús Israel Anaya Salazar
 */
public abstract class CRUDAdapter<DTO extends EntityDTO, E extends Entity, S extends EntityService<E>>
		extends EntityServiceAdapter<DTO, E, S> implements CRUDService<DTO> {

	protected CRUDAdapter(final S service, DataConverter<DTO, E> dataConverter) {
		super(service, dataConverter);
	}
	
	@Override
	public DTO save(DTO dto) {
		validateObject(dto);
		E entity = toEntity(dto);
		E result = getService().save(entity);
		return toDataTransferObject(result);
	}

	@Override
	public DTO findById(String id) {
		E foundItem = getService().findById(id);
		if (foundItem == null) {
			throw DataNotFoundException.fromId(id);
		}
		return toDataTransferObject(foundItem);
	}

	@Override
	public boolean existsById(String id) {
		return getService().existsById(id);
	}

	@Override
	public List<DTO> findAll() {
		ArrayList<DTO> data = new ArrayList<>();

		List<E> entities = getService().findAll();
		for (E entity : entities) {
			data.add(toDataTransferObject(entity));
		}
		return data;
	}

	@Override
	public void deleteById(String id) {
		getService().deleteById(id);
	}
}
