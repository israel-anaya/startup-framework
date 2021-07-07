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
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.startupframework.dto.DataTransferObject;

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
	public DTO save(DTO dto) {
		buffer.put(dto.getId(), dto);
		return dto;
	}

	@Override
	public DTO findById(String id) {
		return buffer.get(id);
	}

	@Override
	public boolean existsById(String id) {
		return buffer.containsKey(id);
	}

	@Override
	public List<DTO> findAll() {
		ArrayList<DTO> data = new ArrayList<>();
		data.addAll(buffer.values());
		return data;
	}

	@Override
	public void deleteById(String id) {
		buffer.remove(id);
	}
}
