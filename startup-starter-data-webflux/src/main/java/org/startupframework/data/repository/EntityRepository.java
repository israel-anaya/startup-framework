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

package org.startupframework.data.repository;

import java.util.Date;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.startupframework.entity.Entity;

import reactor.core.publisher.Flux;

/**
 * Reactive Repository for Entity and inherited.
 *
 * @author Arq. Jesús Israel Anaya Salazar
 */
@NoRepositoryBean
public interface EntityRepository<E extends Entity> extends ReactiveCrudRepository<E, String> {

	Flux<E> findByActive(boolean value, Sort sort);

	Flux<E> findByCreatedDateGreaterThan(Date date);
}
