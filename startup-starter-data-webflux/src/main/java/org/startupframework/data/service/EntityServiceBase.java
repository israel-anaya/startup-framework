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
import java.util.Optional;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.startupframework.data.repository.EntityRepository;
import org.startupframework.entity.Entity;
import org.startupframework.entity.Identifiable;
import org.startupframework.entity.OldNewPred;
import org.startupframework.entity.id.EntityId;
import org.startupframework.entity.id.IdStrategy;
import org.startupframework.exception.DataNotFoundException;
import org.startupframework.validation.ObjectValidatorService;

import lombok.AccessLevel;
import lombok.Getter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service base class with Entity.
 *
 * @author Arq. Jesús Israel Anaya Salazar
 */
@ComponentScan(basePackageClasses = IdStrategy.class)
public abstract class EntityServiceBase<R extends EntityRepository<E>, E extends Entity>
		implements ObjectValidatorService<E>, EntityService<E> {

	static final String ASSERT_REPOSITORY = "Should implements repository for %s";
	static final String ID_NAME = "id";

	@Getter(value = AccessLevel.PROTECTED)
	private final R repository;

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
	
	abstract protected void onValidateObject(E entity);
	
	protected void onBeforeSave(E entity) {
	}

	protected void onAfterSave(E entity) {
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

	/*protected boolean exists(E probe) {
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
	}*/

	protected void generateId(E entity) {
		if (entity.getId() == null) {
			Class<? extends IdStrategy> clazz = entityId.strategy();
			IdStrategy idStrategy = applicationContext.getBean(clazz);
			String id = idStrategy.generate(entityId.value());
			entity.setId(id);
		}
	}

	public void validateObject(E entity) {
		validateObjectConstraints(entity);
		onValidateObject(entity);	
	}
	
	@Override
	public Mono<E> save(E entity) {
		updateDateTime(entity);
		generateId(entity);
		validateObject(entity);
		onBeforeSave(entity);
		Mono<E> result = getRepository().save(entity);
		onAfterSave(entity);
		return result;
	}

	@Override
	public Mono<E> findById(String id) {
		Identifiable.validate(id, ID_NAME);
		Mono<E> foundItem = getRepository().findById(id);
		return foundItem.switchIfEmpty(Mono.error(DataNotFoundException.fromId(id)));
	}

	@Override
	public Mono<Boolean> existsById(String id) {
		Identifiable.validate(id, ID_NAME);
		return getRepository().existsById(id);
	}

	@Override
	public Flux<E> findAll() {
		return getRepository().findAll();
	}

	@Override
	public Flux<E> findAllById(Iterable<String> ids) {
		return getRepository().findAllById(ids);
	}

	@Override
	public Mono<Long> count() {
		return getRepository().count();
	}

	@Override
	public Mono<Void> deleteById(String id) {
		Identifiable.validate(id, ID_NAME);
		return getRepository().deleteById(id);
	}

	@Override
	public Mono<Void> delete(E entity) {
		return getRepository().delete(entity);
	}

	@Override
	public Flux<E> findByActive(boolean value) {
		Sort sort = activeSort();
		return getRepository().findByActive(value, sort);
	}

	@Override
	public Flux<E> findByCreatedDateGreaterThan(Date date) {
		return getRepository().findByCreatedDateGreaterThan(date);
	}

}
