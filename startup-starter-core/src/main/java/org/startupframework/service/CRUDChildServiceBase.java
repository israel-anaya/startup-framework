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

import lombok.Getter;

/**
 * Child Service base class with DTO.
 *
 * @author Arq. Jesús Israel Anaya Salazar
 */
public abstract class CRUDChildServiceBase<DTO extends DataTransferObject>
		implements ObjectValidatorService<DTO>, CRUDChildService<DTO> {

	@Getter
	boolean isAggregated;

	protected CRUDChildServiceBase(boolean isAggregated) {
		super();
		this.isAggregated = isAggregated;
	}

	protected void onBeforeSave(DTO dto) {
	}

	protected void onAfterSave(DTO dto) {
	}

	abstract protected void onValidateObject(DTO dto);

	abstract protected DTO onSave(String parentId, String childId, DTO dto);

	abstract protected List<DTO> onFindAll(String parentId);

	abstract protected Optional<DTO> onFindById(String parentId, String childId);

	abstract protected void onDeleteById(String parentId, String childId);

	void validateIds(String parentId, String childId) {
		Identifiable.validate(parentId, "parentId");
		Identifiable.validate(childId, "childId");
	}

	public void validateObject(DTO dto) {
		validateObjectConstraints(dto);
		onValidateObject(dto);
	}

	@Override
	final public DTO save(String parentId, String childId, DTO dto) {

		Identifiable.validate(parentId, "parentId");

		if (!isAggregated) {
			Identifiable.validate(childId, "childId");
		}

		validateObject(dto);
		onBeforeSave(dto);
		DTO result = onSave(parentId, childId, dto);
		onAfterSave(dto);
		return result;

	}

	@Override
	final public List<DTO> findAll(String parentId) {
		Identifiable.validate(parentId, "parentId");
		return onFindAll(parentId);
	}

	@Override
	final public DTO findById(String parentId, String childId) {
		validateIds(parentId, childId);
		Optional<DTO> foundItem = onFindById(parentId, childId);
		return foundItem.orElseThrow(() -> DataNotFoundException.fromId(childId));
	}

	@Override
	final public void deleteById(String parentId, String childId) {
		validateIds(parentId, childId);
		onDeleteById(parentId, childId);
	}
}
