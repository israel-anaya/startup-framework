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
import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.startupframework.data.entity.Entity;

/**
 * Service interface for PersistentObject and inherited.
 *
 * @author Arq. Jesús Israel Anaya Salazar
 */
public interface EntityService<E extends Entity> {

	E save(E entity);

	E findById(String id);

	boolean existsById(String id);

	List<E> findAll();

	List<E> findAllById(Iterable<String> ids);

	Page<E> findAll(Pageable pageable);

	Page<E> findAll(Example<E> example, Pageable pageable);

	long count();

	void deleteById(String id);

	void delete(E entity);

	List<E> findByActive(boolean value);

	List<E> findByCreatedDateGreaterThan(Date date);

}
