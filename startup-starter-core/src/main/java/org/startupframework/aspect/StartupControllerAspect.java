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

package org.startupframework.aspect;

import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.startupframework.config.StartupProperties;
import org.startupframework.exception.DataException;

/**
 * Startup Controller Aspect.
 *
 * @author Arq. Jesús Israel Anaya Salazar
 */
@Aspect
@Component
public class StartupControllerAspect {

	static Logger log = LoggerFactory.getLogger("MICROSERVICE");

	@Value("${spring.application.name}")
	String serviceId;

	@Autowired
	private StartupProperties properties;

	private HttpHeaders getHeades(HttpServletRequest request) {

		Stream<String> buffer = Collections.list(request.getHeaderNames()).stream();
		Collector<String, ?, HttpHeaders> collector = Collectors.toMap(Function.identity(),
				h -> Collections.list(request.getHeaders(h)), (oldValue, newValue) -> newValue, HttpHeaders::new);

		HttpHeaders httpHeaders = buffer.collect(collector);

		return httpHeaders;
	}

	void throwRequiredHeader(String headerName) {
		String msg = String.format("Header %s required.", headerName);
		throw new DataException(msg);
	}

	private void validateHeaders(HttpHeaders httpHeaders) {
		List<String> requiredHeaders = properties.getHeaders().getRequired();
		for (String headerName : requiredHeaders) {
			String headerValue = httpHeaders.getFirst(headerName);
			if (StringUtils.isEmpty(headerValue)) {
				throwRequiredHeader(headerName);
			}
		}
	}

	private String formatHeaders(HttpHeaders httpHeaders) {
		List<String> logHeaders = properties.getHeaders().getLogger();

		Predicate<Entry<String, List<String>>> predicate = e -> logHeaders.contains(e.getKey().toLowerCase());
		Stream<Entry<String, List<String>>> stream = httpHeaders.entrySet().stream().filter(predicate);
		String buffer = stream.map(e -> e.getKey() + ":" + e.getValue()).collect(Collectors.joining(", "));

		return buffer;
	}

	@Around("@within(org.startupframework.controller.StartupController)")
	protected Object aroundExecution(ProceedingJoinPoint joinPoint) throws Throwable {
		long start = System.currentTimeMillis();

		ServletRequestAttributes servlet = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = servlet.getRequest();
		HttpHeaders httpHeaders = getHeades(request);
		String headers = formatHeaders(httpHeaders);
		String method = request.getMethod();
		String url = request.getRequestURI();
		
		try {
			validateHeaders(httpHeaders);


			log.info("ServiceId:{}, Event:Start, Method:{}, URL:{}, {}", serviceId, method, url, headers);

			Object proceed = joinPoint.proceed();

			long executionTime = System.currentTimeMillis() - start;

			log.info("ServiceId:{}, Event:End, Method:{}, URL:{}, {}, Time:{}ms", serviceId, method, url, headers,
					executionTime);
			
			return proceed;			
		} catch (Throwable ex) {
			log.warn("ServiceId:{}, Event:Exception, Method:{}, URL:{}, Error:{}", serviceId, method, url, ex.getMessage());
			ex.printStackTrace();
			throw ex;
				
		}

	}
}
