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
import org.startupframework.adapter.CRUDChildAdapter;
import org.startupframework.controller.CRUDChildController;
import org.startupframework.controller.StartupController;
import org.startupframework.controller.StartupEndpoint;
import org.startupframework.dto.DataTransferObjectChild;

import lombok.Getter;

/**
 * Base Child Controller with Adapter.
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
public abstract class CRUDChildControllerBase<DTO extends DataTransferObjectChild, AD extends CRUDChildAdapter<DTO>>
		extends StartupEndpoint implements CRUDChildController<DTO> {

	static final String ASSERT_SERVICE = "Should implements adapter for %s";

	@Getter
	final AD adapter;

	protected CRUDChildControllerBase(AD adapter) {
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
	public @ResponseBody ResponseEntity<List<DTO>> getAllItems(@PathVariable("parentId") String parentId) {
		List<DTO> data = adapter.findAll(parentId);

		if (data.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(data, HttpStatus.OK);
		}
	}

	// -------------------Retrieve Single----------------------
	public @ResponseBody ResponseEntity<DTO> getItem(@PathVariable("parentId") String parentId,
			@PathVariable("childId") String childId) {
		DTO item = adapter.findById(parentId, childId);
		return new ResponseEntity<>(item, HttpStatus.OK);
	}

	// -------------------Create a Item----------------------
	public ResponseEntity<DTO> createItem(@PathVariable("parentId") String parentId, @RequestBody DTO item) {
		DTO newItem = adapter.save(parentId, item.getChildId(), item);
		return new ResponseEntity<>(newItem, HttpStatus.CREATED);
	}

	// ------------------- Update an Item----------------------
	public ResponseEntity<DTO> updateItem(@PathVariable("parentId") String parentId, @RequestBody DTO item) {
		DTO currentItem = adapter.findById(parentId, item.getChildId());

		updateProperties(item, currentItem);

		DTO updatedItem = adapter.save(parentId, item.getChildId(), currentItem);
		return new ResponseEntity<>(updatedItem, HttpStatus.OK);
	}

	// ------------------- Delete an Item----------------------
	public @ResponseBody ResponseEntity<DTO> deleteItem(@PathVariable("parentId") String parentId,
			@PathVariable("childId") String childId) {
		DTO deleteItem = adapter.findById(parentId, childId);
		adapter.deleteById(parentId, childId);
		return new ResponseEntity<>(deleteItem, HttpStatus.OK);
	}
}
