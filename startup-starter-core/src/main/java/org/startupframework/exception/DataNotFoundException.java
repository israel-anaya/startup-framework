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

package org.startupframework.exception;

/**
 *
 * @author Arq. Jesús Israel Anaya Salazar
 */
@ErrorCode(StartupErrorCode.NOT_FOUND_DATA)
public class DataNotFoundException extends DataException {

	private static final long serialVersionUID = 1L;

	static public DataNotFoundException fromId(String id) {
		String msg = String.format("Item '%s' no found.", id);
		return new DataNotFoundException(msg);
	}
	
	static public DataNotFoundException fromIds(String parentId, String childId) {
		String msg = String.format("Item '%s' or '%s' no found.", parentId, childId);
		return new DataNotFoundException(msg);
	}

	static public DataNotFoundException from(Object value) {
		String msg = String.format("Item '%s' no found.", value.toString());
		return new DataNotFoundException(msg);
	}

	protected DataNotFoundException() {
		super();
	}

	public DataNotFoundException(String message) {
		super(message);
	}

	public DataNotFoundException(Throwable cause) {
		super(cause);
	}

	public DataNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
