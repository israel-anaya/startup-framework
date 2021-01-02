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

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.startupframework.config.StartupProperties;
import org.startupframework.exception.DataException;
import org.startupframework.log.HttpHeadersHelper;
import org.startupframework.log.HttpLoggerData;
import org.startupframework.log.HttpServiceLogger;

/**
 * Startup Controller Aspect.
 *
 * @author Arq. Jesús Israel Anaya Salazar
 */
@Aspect
@Component
public class StartupControllerAspect {

	static Logger log = LoggerFactory.getLogger("MICROSERVICE");

	private StartupProperties properties;
	HttpServiceLogger httpServiceLogger;

	public StartupControllerAspect(StartupProperties properties, HttpServiceLogger httpServiceLogger) {
		this.properties = properties;
		this.httpServiceLogger = httpServiceLogger;
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

	@Around("@within(org.startupframework.controller.StartupController)")
	protected Object aroundExecution(ProceedingJoinPoint joinPoint) throws Throwable {
		long start = System.currentTimeMillis();

		ServletRequestAttributes servlet = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = servlet.getRequest();
		HttpServletResponse response = servlet.getResponse();

		HttpHeaders requestHeaders = HttpHeadersHelper.getHttRequestHeaders(request);
		HttpLoggerData httpLoggerData = httpServiceLogger.prepareData("Start", request, requestHeaders);

		try {

			validateHeaders(requestHeaders);

			httpServiceLogger.LogEvent(httpLoggerData);

			Object proceed = joinPoint.proceed();
			long elapsedTime = System.currentTimeMillis() - start;

			HttpHeaders responseHeaders = HttpHeadersHelper.getHttResponseHeaders(response);
			httpLoggerData.setEventType("End");
			httpLoggerData.getResponse().setHeaders(responseHeaders);
			httpLoggerData.getResponse().setElapsedTime(elapsedTime);
			httpServiceLogger.LogEvent(httpLoggerData);

			return proceed;
		} catch (Throwable ex) {
			long elapsedTime = System.currentTimeMillis() - start;
			HttpHeaders responseHeaders = HttpHeadersHelper.getHttResponseHeaders(response);			
			httpLoggerData.setEventType("Error");
			httpLoggerData.getResponse().setHeaders(responseHeaders);
			httpLoggerData.getResponse().setElapsedTime(elapsedTime);
			httpLoggerData.getResponse().setError(ex.getMessage());
			httpServiceLogger.WarnEvent(httpLoggerData);

			ex.printStackTrace();
			throw ex;
		}

	}
}
