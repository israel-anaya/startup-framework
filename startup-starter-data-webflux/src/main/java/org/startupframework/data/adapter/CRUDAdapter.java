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

import org.startupframework.data.service.EntityService;
import org.startupframework.dto.EntityDTO;
import org.startupframework.entity.DataConverter;
import org.startupframework.entity.Entity;
import org.startupframework.exception.DataNotFoundException;
import org.startupframework.service.CRUDService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
	public Mono<DTO> save(DTO dto) {
		validateObject(dto);
		E entity = toEntity(dto);
		Mono<E> result = getService().save(entity);
		return result.map(item -> toDataTransferObject(item));
	}

	@Override
	public Mono<DTO> findById(String id) {
		Mono<E> foundItem = getService().findById(id);
		if (foundItem == null) {
			throw DataNotFoundException.fromId(id);
		}
		return foundItem.map(item -> toDataTransferObject(item));
	}

	@Override
	public Mono<Boolean> existsById(String id) {
		return getService().existsById(id);
	}

	@Override
	public Flux<DTO> findAll() {
		Flux<E> entities = getService().findAll();	
		Flux<DTO> data = entities.map(item -> toDataTransferObject(item));
		return data;
	}

	@Override
	public void deleteById(String id) {
		getService().deleteById(id);
	}
}
