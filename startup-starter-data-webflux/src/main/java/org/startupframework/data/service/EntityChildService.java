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

import org.startupframework.entity.Entity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Child Service with Entity.
 *
 * @author Arq. Jesús Israel Anaya Salazar
 */
public interface EntityChildService<E extends Entity> extends EntityService<E> {
	
	abstract Mono<E> save(String parentId, String childId, E entity);

	abstract Mono<E> findById(String parentId, String childId);

	abstract Flux<E> findAll(String parentId);

	abstract Mono<Void> deleteById(String parentId, String childId);
}
