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

package org.startupframework.data.entity;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 *
 * @author Arq. Jesús Israel Anaya Salazar
 */
@Converter
public class IntegerEnumJpaConverter<E extends Enum<E> & IntegerEnum> implements AttributeConverter<E, Integer> {

	private final Class<E> clazz;

	protected IntegerEnumJpaConverter(Class<E> clazz) {
		this.clazz = clazz;
	}

	@Override
	public Integer convertToDatabaseColumn(E enumItem) {
		if (enumItem == null) {
			return null;
		}
		return enumItem.getValue();
	}

	@Override
	public E convertToEntityAttribute(Integer value) {
		if (value == null) {
			return null;
		}

		E[] enums = clazz.getEnumConstants();
		Predicate<E> predicate = t -> t.getValue() == value;
		Stream<E> stream = Stream.of(enums);
		Optional<E> item = stream.filter(predicate).findAny();
		String msg = String.format("%s is ilegan value for type %s", value.toString(), clazz.toString());
		return item.orElseThrow(() -> new IllegalArgumentException(msg));
	}
}