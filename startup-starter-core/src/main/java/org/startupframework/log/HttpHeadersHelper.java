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

package org.startupframework.log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;

/**
 * 
 *
 * @author Arq. Jesús Israel Anaya Salazar
 */
public final class HttpHeadersHelper {

	/**
	 * Gets the headers.
	 *
	 * @param request the request
	 * @return the headers with header name as lower case
	 */
	private static HttpHeaders getHttpHeaders(Supplier<List<String>> headerNames,
			Function<String, List<String>> headerValues) {

		Function<String, String> keyMapper = key -> key.toLowerCase();
		Function<String, List<String>> valueMapper = headerValues;
		BinaryOperator<List<String>> mergeFunction = (oldValue, newValue) -> newValue;
		Supplier<HttpHeaders> mapSupplier = HttpHeaders::new;

		Collector<String, ?, HttpHeaders> collector = Collectors.toMap(keyMapper, valueMapper, mergeFunction,
				mapSupplier);

		Stream<String> buffer = headerNames.get().stream();
		HttpHeaders httpHeaders = buffer.collect(collector);

		return httpHeaders;
	}

	public static HttpHeaders getHttRequestHeaders(HttpServletRequest request) {

		Supplier<List<String>> headerNames = () -> Collections.list(request.getHeaderNames());
		Function<String, List<String>> headerValues = header -> Collections.list(request.getHeaders(header));

		return getHttpHeaders(headerNames, headerValues);
	}

	public static HttpHeaders getHttResponseHeaders(HttpServletResponse response) {

		Supplier<List<String>> headerNames = () -> new ArrayList<>(response.getHeaderNames());
		Function<String, List<String>> headerValues = header -> new ArrayList<>(response.getHeaders(header));

		return getHttpHeaders(headerNames, headerValues);
	}

}
