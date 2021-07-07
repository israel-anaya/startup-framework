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

import java.io.Serializable;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import lombok.Data;

/**
*
* @author Arq. Jesús Israel Anaya Salazar
*/
@Data
public class PageRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	int pageNumber;
	int pageSize;

	String sortDirection;
	String sortProperty;

	public Pageable createPageable() {
		Sort sort = null;
		if (sortProperty != null && !sortProperty.isEmpty()) {
			sort = Sort.by(Direction.fromString(sortDirection), sortProperty);
		}

		return org.springframework.data.domain.PageRequest.of(pageNumber, pageSize, sort);
	}

}
