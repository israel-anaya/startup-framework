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

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatcher;

/**
*
* @author Arq. Jesús Israel Anaya Salazar
*/
public final class ExampleFilter {

	private ExampleFilter() {
		throw new IllegalStateException("Utility class");
	}

	private static List<String> getNestedProperties(String masterProperty, List<String> validProperties) {
		List<String> nestedProperties = new ArrayList<>();
		for (String validProperty : validProperties) {
			if (validProperty.startsWith(masterProperty)) {
				String nestedProperty = validProperty.replaceFirst(masterProperty + ".", "");
				nestedProperties.add(nestedProperty);
			}
		}
		return nestedProperties;
	}

	private static void initPropertyToNull(BeanWrapper bean, PropertyDescriptor property,
			List<String> validProperties) {

		String propertyName = property.getName();
		Class<?> propertyType = property.getPropertyType();

		if (!validProperties.contains(propertyName) && bean.isWritableProperty(propertyName)) {

			if (BeanUtils.isSimpleProperty(propertyType)) {
				bean.setPropertyValue(property.getName(), null);
			} else {
				if (Entity.class.isAssignableFrom(propertyType)) {
					Entity nestedProperty = (Entity) bean.getPropertyValue(propertyName);
					if (nestedProperty != null) {
						initPropertiesToNull(nestedProperty, getNestedProperties(propertyName, validProperties));
					}
				}
			}
		}
	}

	private static void initPropertiesToNull(Entity item, List<String> validProperties) {
		BeanWrapper bean = new BeanWrapperImpl(item);
		PropertyDescriptor[] properties = bean.getPropertyDescriptors();

		for (PropertyDescriptor property : properties) {
			initPropertyToNull(bean, property, validProperties);
		}
	}

	private static GenericPropertyMatcher createFilterMatcher(String matchMode) {
		return ExampleMatcher.GenericPropertyMatchers.contains();
	}

	public static <T extends Entity> Example<T> createExample(RequestInfo<T> requestInfo) {
		List<String> validProperties = new ArrayList<>();
		ExampleMatcher matcher = ExampleMatcher.matching();
		matcher = matcher.withIgnoreCase();
		matcher = matcher.withIgnorePaths("id");
		
		for (FilterRequest filterRequest : requestInfo.getFilters()) {
			GenericPropertyMatcher filterMatcher = createFilterMatcher(filterRequest.getMatchMode());
			matcher = matcher.withMatcher(filterRequest.getProperty(), filterMatcher);
			validProperties.add(filterRequest.getProperty());
		}

		initPropertiesToNull(requestInfo.getFilterValues(), validProperties);

		return Example.of(requestInfo.getFilterValues(), matcher);
	}
}
