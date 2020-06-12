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

package org.startupframework.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.startupframework.dto.DataTransferObject;

/**
 * RestController for DataTransferObjects and inherited.
 * 
 * GET (get a single item or a collection)
 * 
 * POST (add an item to a collection) 
 * 
 * PUT (edit an item that already exists in a collection) 
 * 
 * DELETE (delete an item in a collection)
 * 
 * @author Arq. Jesús Israel Anaya Salazar
 */
@CrossOrigin(origins = "*")
public interface CRUDControllerClient<DTO extends DataTransferObject> {

	static final String URL_BASE = "/";

	@GetMapping(URL_BASE)
	List<DTO> getAllItems();

	@GetMapping(value = URL_BASE + "{id}")
	DTO getItem(@PathVariable("id") String id);

	@PostMapping(value = URL_BASE)
	DTO createItem(@RequestBody DTO item);

	@PutMapping(value = URL_BASE)
	DTO updateItem(@RequestBody DTO item);

}
