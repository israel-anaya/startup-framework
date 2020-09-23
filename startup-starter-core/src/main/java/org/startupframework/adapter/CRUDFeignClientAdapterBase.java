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

package org.startupframework.adapter;

import java.util.ArrayList;
import java.util.List;

import org.startupframework.controller.CRUDControllerClient;
import org.startupframework.dto.DTOConverter;
import org.startupframework.dto.DataTransferObject;
import org.startupframework.exception.DataException;
import org.startupframework.exception.DataNotFoundException;

import lombok.Getter;

/**
 *
 * @author Arq. Jesús Israel Anaya Salazar
 */
public class CRUDFeignClientAdapterBase<S extends DataTransferObject, T extends DataTransferObject, FC extends CRUDControllerClient<S>>
		implements CRUDFeignClientAdapter<T> {

	static final String ASSERT_SERVICE = "Should implements Feign client for %s";

	DTOConverter<S, T> converter;

	@Getter
	final FC feignClient;

	protected CRUDFeignClientAdapterBase(final FC feignClient, DTOConverter<S, T> dataConverter) {
		assert feignClient != null : String.format(ASSERT_SERVICE, this.getClass().getName());
		this.feignClient = feignClient;
		this.converter = dataConverter;
	}

	protected T toTarget(S source) {
		try {
			return converter.toTarget(source);
		} catch (Exception ex) {
			throw new DataException(ex);
		}
	}

	protected S toSource(T target) {
		try {
			return converter.toSource(target);
		} catch (Exception ex) {
			throw new DataException(ex);
		}
	}

	@Override
	public List<T> getAllItems() {
		ArrayList<T> data = new ArrayList<>();

		List<S> entities = getFeignClient().getAllItems();

		if (entities == null) {
			return data;
		}

		for (S source : entities) {
			data.add(toTarget(source));
		}
		return data;
	}

	@Override
	public List<T> getAllActiveItems() {
		ArrayList<T> data = new ArrayList<>();

		List<S> entities = getFeignClient().getAllActiveItems();

		if (entities == null) {
			return data;
		}

		for (S source : entities) {
			data.add(toTarget(source));
		}
		return data;
	}

	@Override
	public T createItem(T item) {
		S source = toSource(item);
		S result = getFeignClient().createItem(source);
		return toTarget(result);
	}

	@Override
	public T getItem(String id) {
		S foundItem = getFeignClient().getItem(id);
		if (foundItem == null) {
			throw DataNotFoundException.fromId(id);
		}
		return toTarget(foundItem);
	}

	@Override
	public T updateItem(T item) {
		S source = toSource(item);
		S result = getFeignClient().createItem(source);
		return toTarget(result);
	}

}
