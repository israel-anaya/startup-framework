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

/**
*
* @author Arq. Jesús Israel Anaya Salazar
*/
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.startupframework.adapter.CRUDAdapter;
import org.startupframework.data.repository.EntityRepository;
import org.startupframework.dto.DataTransferObject;
import org.startupframework.entity.Entity;
import org.startupframework.exception.DataNotFoundException;

import lombok.AccessLevel;
import lombok.Getter;

public abstract class RepositoryAdapter<DTO extends DataTransferObject, E extends Entity, R extends EntityRepository<E>>
		implements CRUDAdapter<DTO> {

	static final String ASSERT_REPOSITORY = "Should implements repository for %s";
	
	@Getter(value = AccessLevel.PROTECTED)
	private final R repository;

	protected RepositoryAdapter(final R repository) {
		assert repository!= null : String.format(ASSERT_REPOSITORY, this.getClass().getName());
		this.repository = repository;
	}

	protected E toEntity(DTO dto) {
		return null;
	}

	protected DTO toDataTransferObject(E entity) {
		return null;
	}

	@Override
	public DTO save(DTO dto) {
		E entity = toEntity(dto);
		E result = getRepository().save(entity);
		return toDataTransferObject(result);
	}

	@Override
	public DTO findById(String id) {
		Optional<E> foundItem = getRepository().findById(id);
		E result = foundItem.orElseThrow(() -> DataNotFoundException.fromId(id));
		return toDataTransferObject(result);
	}

	@Override
	public boolean existsById(String id) {
		return getRepository().existsById(id);
	}

	@Override
	public List<DTO> findAll() {
		ArrayList<DTO> data = new ArrayList<>();

		List<E> entities = getRepository().findAll();
		for (E entity : entities) {
			data.add(toDataTransferObject(entity));
		}
		return data;
	}

	@Override
	public void deleteById(String id) {
		getRepository().deleteById(id);
	}
}
