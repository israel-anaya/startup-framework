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

package org.startupframework.service.feign;

import java.util.ArrayList;
import java.util.List;

import org.startupframework.dto.DTOConverter;
import org.startupframework.dto.DataTransferObjectChild;
import org.startupframework.exception.DataException;
import org.startupframework.validation.ObjectValidatorService;

import lombok.AccessLevel;
import lombok.Getter;

/**
 *
 * @author Arq. Jesús Israel Anaya Salazar
 */
public abstract class CRUDChildFeignBase<S extends DataTransferObjectChild, T extends DataTransferObjectChild>
		implements ObjectValidatorService<T>, CRUDChildFeign<T> {

	static final String ASSERT_SERVICE = "Should implements Feign client for %s";

	final DTOConverter<S, T> converter;

	@Getter(value = AccessLevel.PROTECTED)
	private final CRUDChildFeign<S> feign;

	protected CRUDChildFeignBase(final CRUDChildFeign<S> feign, final DTOConverter<S, T> dataConverter) {
		assert feign != null : String.format(ASSERT_SERVICE, this.getClass().getName());
		this.feign = feign;
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

	abstract protected void onValidateObject(T dto);

	public void validateObject(T dto) {
		validateObjectConstraints(dto);
		onValidateObject(dto);
	}

	@Override
	public List<T> getAllItems(String parentId) {
		ArrayList<T> data = new ArrayList<>();

		List<S> entities = getFeign().getAllItems(parentId);

		if (entities == null) {
			return data;
		}

		for (S source : entities) {
			data.add(toTarget(source));
		}
		return data;
	}

	@Override
	public T getItem(String parentId, String childId) {
		S foundItem = getFeign().getItem(parentId, childId);
		return toTarget(foundItem);
	}

	@Override
	public T createItem(String parentId, T item) {
		validateObject(item);
		S source = toSource(item);
		S result = getFeign().createItem(parentId, source);
		return toTarget(result);
	}

	@Override
	public T updateItem(String parentId, T item) {
		validateObject(item);
		S source = toSource(item);
		S result = getFeign().updateItem(parentId, source);
		return toTarget(result);
	}

	@Override
	public T deleteItem(String parentId, String childId) {
		S result = getFeign().deleteItem(parentId, childId);
		return toTarget(result);
	}

}
