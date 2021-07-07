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

package org.startupframework.service;

import org.startupframework.dto.DataTransferObject;
import org.startupframework.entity.Identifiable;
import org.startupframework.exception.DataNotFoundException;
import org.startupframework.validation.ObjectValidatorService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service base class with DTO.
 *
 * @author Arq. Jesús Israel Anaya Salazar
 */
public abstract class CRUDServiceBase<DTO extends DataTransferObject>
		implements ObjectValidatorService<DTO>, CRUDService<DTO> {

	static final String ID_NAME = "id";

	protected void onBeforeSave(DTO dto) {
	}

	protected void onAfterSave(DTO dto) {
	}

	abstract protected void onValidateObject(DTO dto);
	
	abstract protected Mono<DTO> onSave(DTO dto);

	abstract protected Mono<DTO> onFindById(String id);

	abstract protected Flux<DTO> onFindAll();

	abstract protected void onDeleteById(String id);

	
	public void validateObject(DTO dto) {
		validateObjectConstraints(dto);
		onValidateObject(dto);	
	}
	
	@Override
	final public Mono<DTO> save(DTO dto) {
		validateObject(dto);
		onBeforeSave(dto);
		Mono<DTO> result = onSave(dto);
		onAfterSave(dto);
		return result;
	}

	@Override
	final public Mono<DTO> findById(String id) {
		Identifiable.validate(id, ID_NAME);
		Mono<DTO> foundItem = onFindById(id);
		
		return foundItem.switchIfEmpty(Mono.error(DataNotFoundException.fromId(id)));
	}

	@Override
	final public Mono<Boolean> existsById(String id) {
		Identifiable.validate(id, ID_NAME);
		Mono<DTO> foundItem = onFindById(id);						
		return foundItem.map(item -> true).defaultIfEmpty(false);
	}

	@Override
	final public Flux<DTO> findAll() {
		return onFindAll();
	}

	@Override
	final public void deleteById(String id) {
		Identifiable.validate(id, ID_NAME);
		onDeleteById(id);
	}

}
