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

package org.startupframework.data.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

import org.startupframework.data.entity.OldNewPred;
import org.startupframework.data.repository.EntityRepository;
import org.startupframework.entity.Entity;
import org.startupframework.entity.Identifiable;
import org.startupframework.exception.DataNotFoundException;
import org.startupframework.exception.DuplicateDataException;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * Child Service base class with Entity.
 *
 * @author Arq. Jesús Israel Anaya Salazar
 */
public abstract class EntityServiceChildBase<R extends EntityRepository<E>, E extends Entity>
		extends EntityServiceBase<R, E> implements EntityServiceChild<E> {

	@Getter(value = AccessLevel.PROTECTED)
	private boolean isAggregated;

	protected EntityServiceChildBase(final R repository, boolean isAggregated) {
		super(repository);
		this.isAggregated = isAggregated;
	}

	abstract protected String getParentId(E entity);

	abstract protected void setParentId(String parentId, E entity);

	abstract protected String getChildId(E entity);

	abstract protected void setChildId(String childId, E entity);

	abstract protected List<E> onFindAll(String parentId);

	abstract protected Optional<E> onFindById(String parentId, String childId);

	void validateIds(String parentId, String childId) {
		Identifiable.validate(parentId, "parentId");
		Identifiable.validate(childId, "childId");
	}

	final OldNewPred<E> NEED_VALIDATE = (o, n) -> !Objects.equals(getChildId(o), getChildId(n));

	@Override
	protected void onValidateObject(E entity) {

		Consumer<E> validation = e -> {

			String parentId = getParentId(e);
			String childId = getChildId(e);

			Optional<E> foundItem = onFindById(parentId, childId);
			foundItem.ifPresent(x -> {
				throw DuplicateDataException.from(childId);
			});
		};

		Optional<E> oldEntity = getRepository().findById(entity.getId());
		validateIf(NEED_VALIDATE, validation, oldEntity, entity);

	}

	@Override
	final public E save(String parentId, String childId, E entity) {

		Identifiable.validate(parentId, "parentId");
		setParentId(parentId, entity);

		if (!isAggregated) {
			Identifiable.validate(childId, "childId");
			setChildId(childId, entity);
		}
		return this.save(entity);

	}

	@Override
	final public List<E> findAll(String parentId) {
		Identifiable.validate(parentId, "parentId");
		return onFindAll(parentId);
	}

	@Override
	final public E findById(String parentId, String childId) {
		validateIds(parentId, childId);
		Optional<E> foundItem = onFindById(parentId, childId);
		return foundItem.orElseThrow(() -> DataNotFoundException.fromIds(parentId, childId));
	}

	@Override
	final public void deleteById(String parentId, String childId) {
		validateIds(parentId, childId);
		E buffer = findById(parentId, childId);
		this.delete(buffer);
	}
}
