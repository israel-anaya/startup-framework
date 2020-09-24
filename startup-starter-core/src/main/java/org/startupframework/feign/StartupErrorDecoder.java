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

package org.startupframework.feign;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.startupframework.entity.ErrorInfo;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;

/**
 *
 * @author Arq. Jesús Israel Anaya Salazar
 */
public class StartupErrorDecoder implements ErrorDecoder {

	private final ErrorDecoder errorDecoder = new Default();

	public StartupErrorDecoder() {
	}

	Exception createException(ErrorInfo errorInfo)
			throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		Class<?> clazz = Class.forName(errorInfo.getException());
		Constructor<?> ctor = clazz.getConstructor(String.class);
		Exception exception = (Exception) ctor.newInstance(new Object[] { errorInfo.getErrorMessage() });
		return exception;
	}

	@Override
	public Exception decode(String methodKey, Response response) {

		if (response.status() >= HttpStatus.BAD_REQUEST.value()) {
			// ErrorInfo errorInfo;
			Reader reader = null;

			try {
				reader = response.body().asReader(StandardCharsets.UTF_8);
				String result = IOUtils.toString(reader);
				ObjectMapper mapper = new ObjectMapper();
				mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

				ErrorInfo errorInfo = mapper.readValue(result, ErrorInfo.class);

				return createException(errorInfo);

			} catch (FeignException | IOException | ClassNotFoundException | NoSuchMethodException | SecurityException
					| InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				return e;
			}
		}

		return errorDecoder.decode(methodKey, response);
	}

}
