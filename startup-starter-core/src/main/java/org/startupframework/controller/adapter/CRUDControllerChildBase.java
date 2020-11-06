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
import org.startupframework.adapter.CRUDAdapterChild;
import org.startupframework.controller.CRUDControllerChild;
import org.startupframework.controller.StartupController;
import org.startupframework.controller.StartupEndpoint;
import org.startupframework.dto.DataTransferObjectChild;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * Base Child Controller with Adapter.
 * 
 * @author Arq. Jesús Israel Anaya Salazar
 */
@StartupController
public abstract class CRUDControllerChildBase<DTO extends DataTransferObjectChild, AD extends CRUDAdapterChild<DTO>>
		extends StartupEndpoint implements CRUDControllerChild<DTO> {

	static final String ASSERT_SERVICE = "Should implements adapter for %s";

	@Getter(value = AccessLevel.PROTECTED)
	private final AD adapter;

	protected CRUDControllerChildBase(AD adapter) {
		assert adapter != null : String.format(ASSERT_SERVICE, this.getClass().getName());
		this.adapter = adapter;
	}

	protected <P> void updateProperty(Supplier<P> source, Consumer<P> target) {
		P value = source.get();
		if (value != null)
			target.accept(value);
	}

	abstract protected void updateProperties(DTO source, DTO target);

	@Override
	public ResponseEntity<List<DTO>> getAllItems(String parentId) {
		List<DTO> data = adapter.findAll(parentId);

		if (data.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(data, HttpStatus.OK);
		}
	}

	@Override
	public ResponseEntity<DTO> getItem(String parentId, String childId) {
		DTO item = adapter.findById(parentId, childId);
		return new ResponseEntity<>(item, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<DTO> createItem(String parentId, DTO item) {
		DTO newItem = adapter.save(parentId, item.getChildId(), item);
		return new ResponseEntity<>(newItem, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<DTO> updateItem(String parentId, DTO item) {
		DTO currentItem = adapter.findById(parentId, item.getChildId());

		updateProperties(item, currentItem);

		DTO updatedItem = adapter.save(parentId, item.getChildId(), currentItem);
		return new ResponseEntity<>(updatedItem, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<DTO> deleteItem(String parentId, String childId) {
		DTO deleteItem = adapter.findById(parentId, childId);
		adapter.deleteById(parentId, childId);
		return new ResponseEntity<>(deleteItem, HttpStatus.OK);
	}
}
