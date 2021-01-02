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

import java.util.List;
import java.util.Map.Entry;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.startupframework.config.StartupProperties;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.logstash.logback.argument.StructuredArguments;

/**
 * 
 *
 * @author Arq. Jesús Israel Anaya Salazar
 */
@Component
public class HttpServiceLoggerDefault implements HttpServiceLogger {

	static Logger logger = LoggerFactory.getLogger("MICROSERVICE");
	static ObjectMapper objectMapper = new ObjectMapper();

	private StartupProperties properties;

	public HttpServiceLoggerDefault(StartupProperties properties) {
		this.properties = properties;
		objectMapper.setSerializationInclusion(Include.NON_NULL);

	}

	private HttpHeaders getHttpHeadersForLog(HttpHeaders httpHeaders) {

		Stream<String> logHeadersStream = properties.getHeaders().getLogger().stream();
		List<String> logHeaders = logHeadersStream.map(header -> header.toLowerCase()).collect(Collectors.toList());

		Predicate<Entry<String, List<String>>> predicate = e -> logHeaders.contains(e.getKey());
		Stream<Entry<String, List<String>>> buffer = httpHeaders.entrySet().stream().filter(predicate);

		Function<Entry<String, List<String>>, String> keyMapper = entry -> entry.getKey();
		Function<Entry<String, List<String>>, List<String>> valueMapper = entry -> entry.getValue();
		BinaryOperator<List<String>> mergeFunction = (u, v) -> {
			throw new IllegalStateException(String.format("Duplicate key %s", u));
		};
		Supplier<HttpHeaders> mapSupplier = HttpHeaders::new;

		Collector<Entry<String, List<String>>, ?, HttpHeaders> collector;
		collector = Collectors.toMap(keyMapper, valueMapper, mergeFunction, mapSupplier);
		httpHeaders = buffer.collect(collector);

		return httpHeaders;
	}

	public HttpLoggerData prepareData(String event, HttpServletRequest request, HttpHeaders httpHeaders) {

		HttpHeaders httpHeaderForLog = getHttpHeadersForLog(httpHeaders);
		String method = request.getMethod();
		String uri = request.getRequestURI();

		HttpLoggerData loggerData = new HttpLoggerData();
		loggerData.setEventType(event);
		loggerData.getRequest().setMethod(method);
		loggerData.getRequest().setUri(uri);
		loggerData.getRequest().setHeaders(httpHeaderForLog);

		return loggerData;
	}

	public void LogEvent(HttpLoggerData loggerData) throws JsonProcessingException {
		String loggerAsJson = objectMapper.writeValueAsString(loggerData);
		logger.info("{}", StructuredArguments.value(HttpLoggerData.SVC_INFO, loggerAsJson));
	}

	public void WarnEvent(HttpLoggerData loggerData) throws JsonProcessingException {
		String loggerAsJson = objectMapper.writeValueAsString(loggerData);
		logger.warn("{}", StructuredArguments.value(HttpLoggerData.SVC_INFO, loggerAsJson));
	}

}
