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

import lombok.Getter;

/**
 * Service base class for EntityBase and inherited.
 *
 * @author Arq. Jesús Israel Anaya Salazar
 */
public abstract class ChildDataTransferObjectServiceBase<DTO extends DataTransferObject> extends ObjectValidatorService<DTO>
		implements ChildDataTransferObjectService<DTO> {

	@Getter
	boolean isAggregated;

	protected ChildDataTransferObjectServiceBase(boolean isAggregated) {
		super();
		this.isAggregated = isAggregated;
	}

	/*abstract protected String getParentId(DTO dto);

	abstract protected void setParentId(String parentId, DTO dto);

	abstract protected String getChildId(DTO dto);

	abstract protected void setChildId(String childId, DTO dto);*/

	abstract protected DTO onSave(String parentId, String childId, DTO dto);

	abstract protected List<DTO> onFindAll(String parentId);

	abstract protected Optional<DTO> onFindById(String parentId, String childId);

	abstract protected void onDeleteById(String parentId, String childId);

	void validateIds(String parentId, String childId) {
		Identifiable.validate(parentId, "parentId");
		Identifiable.validate(childId, "childId");
	}

	@Override
	final public DTO save(String parentId, String childId, DTO dto) {

		Identifiable.validate(parentId, "parentId");
		//setParentId(parentId, dto);

		if (!isAggregated) {
			Identifiable.validate(childId, "childId");
			//setChildId(childId, dto);
		}

		onBeforeSave(dto);
		validateObjectConstraints(dto);
		onValidateEntity(dto);
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
