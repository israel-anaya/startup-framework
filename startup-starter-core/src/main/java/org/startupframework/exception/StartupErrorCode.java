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

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.annotation.AnnotatedElementUtils;

/**
*
* @author Arq. Jesús Israel Anaya Salazar
*/
public class StartupErrorCode {

	public static final String INVALID_DATA = "invalidData";
	public static final String NOT_FOUND_DATA = "notFoundData";
	public static final String DUPLICATE_DATA = "duplicateData";
	public static final String NULL_ID_DATA = "nullIdData";

	static Map<Class<?>, String> cacheError = new HashMap<>();

	public static String getErrorCode(Exception ex) {
		Class<?> domainType = ex.getClass();
		if (cacheError.containsKey(domainType)) {
			return cacheError.get(domainType);
		}

		ErrorCode errorCode = AnnotatedElementUtils.findMergedAnnotation(domainType, ErrorCode.class);
		if (errorCode == null) {
			throw new IllegalArgumentException("Exception need ErrorCode Annotation");
		}
		cacheError.put(domainType, errorCode.value());
		return errorCode.value();
	}
	
	/*public static Exception (String errorCode) {
		
	}*/
}
