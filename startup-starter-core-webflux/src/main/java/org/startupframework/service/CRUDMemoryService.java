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

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import org.startupframework.dto.DataTransferObject;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service base for DataTransferObject in Memory.
 *
 * @author Arq. Jesús Israel Anaya Salazar
 */
public class CRUDMemoryService<DTO extends DataTransferObject> implements CRUDService<DTO> {

	public CRUDMemoryService() {

	}

	Map<String, DTO> buffer = new TreeMap<>();

	@Override
	public Mono<DTO> save(DTO dto) {
		buffer.put(dto.getId(), dto);
		return Mono.just(dto);
	}

	@Override
	public Mono<DTO> findById(String id) {
		return Mono.just(buffer.get(id));
	}

	@Override
	public Mono<Boolean> existsById(String id) {
		return Mono.just(buffer.containsKey(id));
	}

	@Override
	public Flux<DTO> findAll() {
		ArrayList<DTO> data = new ArrayList<>();
		data.addAll(buffer.values());
		return Flux.fromIterable(buffer.values());
	}

	@Override
	public void deleteById(String id) {
		buffer.remove(id);
	}
}
