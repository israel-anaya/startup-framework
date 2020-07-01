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

package org.startupframework.data.entity.id;

import java.util.UUID;

import org.springframework.stereotype.Component;

/**
*
* @author Arq. Jesús Israel Anaya Salazar
*/
@Component
public class IdPrefixStrategy implements IdStrategy {

	@Override
	public String generate(String value) {
		return createId(value, UUID.randomUUID());
	}

	private String createId(String value, UUID uuid) {
		return value + "-" + uuid.toString();
	}

}
