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

package org.startupframework.controller.feign;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.startupframework.controller.CRUDController;
import org.startupframework.controller.StartupController;
import org.startupframework.controller.StartupEndpoint;
import org.startupframework.dto.DataTransferObject;
import org.startupframework.feign.CRUDFeign;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * Base Controller with Service.
 * 
 * @author Arq. Jesús Israel Anaya Salazar
 */
@StartupController
public abstract class CRUDControllerBase<DTO extends DataTransferObject, F extends CRUDFeign<DTO>> extends StartupEndpoint
		implements CRUDController<DTO> {

	static final String ASSERT_SERVICE = "Should implements Feign client for %s";

	@Getter(value = AccessLevel.PROTECTED)
	private final F feign;

	protected CRUDControllerBase(F feign) {
		assert feign != null : String.format(ASSERT_SERVICE, this.getClass().getName());
		this.feign = feign;
	}

	protected <P> void updateProperty(Supplier<P> source, Consumer<P> target) {
		P value = source.get();
		if (value != null)
			target.accept(value);
	}

	abstract protected void updateProperties(DTO source, DTO target);

	@Override
	public ResponseEntity<List<DTO>> getAllItems() {
		List<DTO> data = feign.getAllItems();

		if (data.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(data, HttpStatus.OK);
		}
	}

	@Override
	public ResponseEntity<DTO> getItem(String id) {
		DTO item = feign.getItem(id);
		return new ResponseEntity<>(item, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<DTO> createItem(DTO item) {
		DTO newItem = feign.createItem(item);
		return new ResponseEntity<>(newItem, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<DTO> updateItem(DTO item) {
		DTO updatedItem = feign.updateItem(item);
		return new ResponseEntity<>(updatedItem, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<DTO> deleteItem(String id) {
		DTO deleteItem = feign.deleteItem(id);
		return new ResponseEntity<>(deleteItem, HttpStatus.OK);
	}
}
