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

package org.startupframework.controller.adapter;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.startupframework.adapter.CRUDAdapter;
import org.startupframework.controller.CRUDController;
import org.startupframework.controller.StartupController;
import org.startupframework.controller.StartupEndpoint;
import org.startupframework.dto.DataTransferObject;

import lombok.Getter;

/**
 * Base Controller with Adapter.
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
@StartupController
public abstract class CRUDControllerBase<DTO extends DataTransferObject, AD extends CRUDAdapter<DTO>>
		extends StartupEndpoint implements CRUDController<DTO> {

	static final String ASSERT_SERVICE = "Should implements adapter for %s";

	@Getter
	final AD adapter;

	protected CRUDControllerBase(AD adapter) {
		assert adapter != null : String.format(ASSERT_SERVICE, this.getClass().getName());
		this.adapter = adapter;
	}

	protected <T> void updateProperty(Supplier<T> source, Consumer<T> target) {
		T value = source.get();
		if (value != null)
			target.accept(value);
	}

	abstract protected void updateProperties(DTO source, DTO target);

	// -------------------Retrieve AllData----------------------
	public @ResponseBody ResponseEntity<List<DTO>> getAllItems() {
		List<DTO> data = adapter.findAll();

		if (data.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(data, HttpStatus.OK);
		}
	}

	// -------------------Retrieve AllActiveData----------------------
	public @ResponseBody ResponseEntity<List<DTO>> getAllActiveItems() {
		List<DTO> data = adapter.findByActive(true);

		if (data.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(data, HttpStatus.OK);
		}
	}

	// -------------------Retrieve Single----------------------
	public @ResponseBody ResponseEntity<DTO> getItem(@PathVariable("id") String id) {
		DTO item = adapter.findById(id);
		return new ResponseEntity<>(item, HttpStatus.OK);
	}

	// -------------------Create a Item----------------------
	public ResponseEntity<DTO> createItem(@RequestBody DTO item) {
		DTO newItem = adapter.save(item);
		return new ResponseEntity<>(newItem, HttpStatus.CREATED);
	}

	// ------------------- Update a Item----------------------
	public ResponseEntity<DTO> updateItem(@RequestBody DTO item) {
		DTO currentItem = null;
		currentItem = adapter.findById(item.getId());

		updateProperties(item, currentItem);

		DTO updatedItem = adapter.save(currentItem);
		return new ResponseEntity<>(updatedItem, HttpStatus.OK);
	}
}
