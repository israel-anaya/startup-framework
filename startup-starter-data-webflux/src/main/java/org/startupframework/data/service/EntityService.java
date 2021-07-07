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

import java.util.Date;

import org.startupframework.entity.Entity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service with Entity.
 *
 * @author Arq. Jesús Israel Anaya Salazar
 */
public interface EntityService<E extends Entity> {

	Mono<E> save(E entity);

	Mono<E> findById(String id);

	Mono<Boolean> existsById(String id);

	Flux<E> findAll();
	
	Flux<E> findAllById(Iterable<String> ids);

	Mono<Long> count();

	Mono<Void> deleteById(String id);

	Mono<Void> delete(E entity);

	Flux<E> findByActive(boolean value);

	Flux<E> findByCreatedDateGreaterThan(Date date);

}
