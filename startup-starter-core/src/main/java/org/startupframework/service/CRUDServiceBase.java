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

import java.util.List;
import java.util.Optional;

import org.startupframework.dto.DataTransferObject;
import org.startupframework.entity.Identifiable;
import org.startupframework.exception.DataNotFoundException;
import org.startupframework.validation.ObjectValidatorService;

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
	
	abstract protected DTO onSave(DTO dto);

	abstract protected Optional<DTO> onFindById(String id);

	abstract protected List<DTO> onFindAll();

	abstract protected void onDeleteById(String id);

	
	public void validateObject(DTO dto) {
		validateObjectConstraints(dto);
		onValidateObject(dto);	
	}
	
	@Override
	final public DTO save(DTO dto) {
		validateObject(dto);
		onBeforeSave(dto);
		DTO result = onSave(dto);
		onAfterSave(dto);
		return result;
	}

	@Override
	final public DTO findById(String id) {
		Identifiable.validate(id, ID_NAME);
		Optional<DTO> foundItem = onFindById(id);
		return foundItem.orElseThrow(() -> DataNotFoundException.fromId(id));
	}

	@Override
	final public boolean existsById(String id) {
		Identifiable.validate(id, ID_NAME);
		Optional<DTO> foundItem = onFindById(id);
		return foundItem.isPresent();
	}

	@Override
	final public List<DTO> findAll() {
		return onFindAll();
	}

	@Override
	final public void deleteById(String id) {
		Identifiable.validate(id, ID_NAME);
		onDeleteById(id);
	}

}
