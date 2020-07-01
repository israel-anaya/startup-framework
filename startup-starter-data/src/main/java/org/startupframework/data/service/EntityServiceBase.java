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

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.persistence.metamodel.SingularAttribute;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.startupframework.data.entity.OldNewPred;
import org.startupframework.data.entity.id.EntityId;
import org.startupframework.data.entity.id.IdStrategy;
import org.startupframework.data.repository.EntityRepository;
import org.startupframework.entity.Entity;
import org.startupframework.entity.Identifiable;
import org.startupframework.exception.DataNotFoundException;
import org.startupframework.exception.DuplicateDataException;
import org.startupframework.service.ObjectValidatorService;

import lombok.Getter;

/**
 * Service base class with Entity.
 *
 * @author Arq. Jesús Israel Anaya Salazar
 */
@ComponentScan(basePackageClasses = IdStrategy.class)
public abstract class EntityServiceBase<R extends EntityRepository<E>, E extends Entity>
		extends ObjectValidatorService<E> implements EntityService<E> {

	static final String ASSERT_REPOSITORY = "Should implements repository for %s";
	static final String ID_NAME = "id";

	@Getter
	final R repository;

	@Autowired
	private ApplicationContext applicationContext;

	EntityId entityId;

	@SuppressWarnings("unchecked")
	@Autowired
	protected EntityServiceBase(final R repository) {
		assert repository != null : String.format(ASSERT_REPOSITORY, this.getClass().getName());
		this.repository = repository;

		Class<?>[] arguments = GenericTypeResolver.resolveTypeArguments(this.getClass(), EntityServiceBase.class);
		Class<E> entityType = (Class<E>) arguments[1];
		entityId = AnnotatedElementUtils.findMergedAnnotation(entityType, EntityId.class);
		if (entityId == null) {
			throw new IllegalArgumentException("Entity need EntityIdPrefix Annotation");
		}
	}
	
	protected void onBeforeSave(E entity) {
	}

	protected void onAfterSave(E entity) {
	}

	protected void existsBy(SingularAttribute<E, ?> attribute, Supplier<Object> supplier) {
		Object value = supplier.get();
		Specification<E> spec = (r, q, b) -> b.equal(r.get(attribute), value);
		if (getRepository().count(spec) != 0)
			throw DuplicateDataException.from(value);
	}

	protected void validateIf(OldNewPred<E> predicate, Consumer<E> validation, Optional<E> oldEntity, E newEntity) {
		if (oldEntity.isPresent()) {
			if (predicate.test(oldEntity.get(), newEntity)) {
				validation.accept(newEntity);
				return;
			}
			return;
		}
		validation.accept(newEntity);
	}

	protected Date getCurrentDate() {
		LocalDateTime localDateTime = LocalDateTime.now();
		Date currentDate = java.sql.Timestamp.valueOf(localDateTime);
		return currentDate;
	}

	protected void updateDateTime(E entity) {
		Date currentDate = getCurrentDate();

		if (entity.getCreatedDate() == null) {
			entity.setCreatedDate(currentDate);
		}
		entity.setModifiedDate(currentDate);
	}

	protected Sort defaultSort() {
		return Sort.by(Direction.ASC, "id");
	}

	protected Sort activeSort() {
		return Sort.by(Direction.ASC, "id");
	}

	protected boolean exists(E probe) {
		Example<E> example = Example.of(probe, ExampleMatcher.matchingAll());
		return getRepository().exists(example);
	}

	protected boolean exists(E probe, ExampleMatcher matcher) {
		Example<E> example = Example.of(probe, matcher);
		return getRepository().exists(example);
	}

	protected E findBy(SingularAttribute<E, ?> attribute, Supplier<Object> supplier) {
		Object value = supplier.get();
		Specification<E> spec = (r, q, b) -> b.equal(r.get(attribute), value);
		Optional<E> foundItem = getRepository().findOne(spec);
		return foundItem.orElseThrow(() -> DataNotFoundException.from(value));
	}

	protected void generateId(E entity) {
		if (entity.getId() == null) {
			Class<? extends IdStrategy> clazz = entityId.strategy();
			IdStrategy idStrategy = applicationContext.getBean(clazz);
			String id = idStrategy.generate(entityId.value());
			entity.setId(id);
		}
	}

	@Override
	public E save(E entity) {
		updateDateTime(entity);
		onBeforeSave(entity);
		generateId(entity);
		validateObjectConstraints(entity);
		onValidateObject(entity);
		E result = getRepository().save(entity);
		onAfterSave(entity);
		return result;
	}

	@Override
	public E findById(String id) {
		Identifiable.validate(id, ID_NAME);
		Optional<E> foundItem = getRepository().findById(id);
		return foundItem.orElseThrow(() -> DataNotFoundException.fromId(id));
	}

	@Override
	public boolean existsById(String id) {
		Identifiable.validate(id, ID_NAME);
		return getRepository().existsById(id);
	}

	@Override
	public List<E> findAll() {
		return getRepository().findAll();
	}

	@Override
	public List<E> findAllById(Iterable<String> ids) {
		return getRepository().findAllById(ids);
	}

	@Override
	public Page<E> findAll(Pageable pageable) {
		return getRepository().findAll(pageable);
	}

	@Override
	public Page<E> findAll(Example<E> example, Pageable pageable) {
		return getRepository().findAll(example, pageable);
	}

	@Override
	public long count() {
		return getRepository().count();
	}

	@Override
	public void deleteById(String id) {
		Identifiable.validate(id, ID_NAME);
		getRepository().deleteById(id);
	}

	@Override
	public void delete(E entity) {
		getRepository().delete(entity);
	}

	@Override
	public List<E> findByActive(boolean value) {
		Sort sort = activeSort();
		return getRepository().findByActive(value, sort);
	}

	@Override
	public List<E> findByCreatedDateGreaterThan(Date date) {
		return getRepository().findByCreatedDateGreaterThan(date);
	}

}
