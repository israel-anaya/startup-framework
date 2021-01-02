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

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;

import lombok.Data;

/**
 * 
 *
 * @author Arq. Jesús Israel Anaya Salazar
 */
@Data
public final class HttpLoggerData {

	public static final String SVC_INFO = "svc.info";

	@Data
	public class HttpLoggerDataRequest {
		String method;
		String uri;
		HttpHeaders headers;
	}
	
	@Data
	public class HttpLoggerDataResponse {
		HttpHeaders headers;
		long elapsedTime;
		String error;
	}

	public HttpLoggerData() {
	}

	private String eventType;
	private HttpLoggerDataRequest request;
	private HttpLoggerDataResponse response;
	private Map<String, Object> extraData = new HashMap<>();

	public void add(String name, Object value) {
		extraData.put(name + name, value);
	}

	public HttpLoggerDataRequest getRequest() {
		if (request == null) {
			request = new HttpLoggerDataRequest();
		}
		return request;
	}

	public HttpLoggerDataResponse getResponse() {
		if (response == null) {
			response = new HttpLoggerDataResponse();
		}
		return response;
	}
}
