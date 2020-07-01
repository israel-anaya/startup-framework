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

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.startupframework.entity.ErrorInfo;
import org.startupframework.exception.StartupException;

import com.fasterxml.jackson.databind.ObjectMapper;

import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;

/**
 *
 * @author Arq. Jesús Israel Anaya Salazar
 */
public class StartupErrorDecoder implements ErrorDecoder {

	ResponseEntityDecoder decoder;

	public StartupErrorDecoder() {
		ObjectMapper objectMapper = new ObjectMapper();
		HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter(objectMapper);
		ObjectFactory<HttpMessageConverters> objectFactory = () -> new HttpMessageConverters(jacksonConverter);
		decoder = new ResponseEntityDecoder(new SpringDecoder(objectFactory));
	}

	@Override
	public Exception decode(String methodKey, Response response) {

		if (response.status() >= HttpStatus.BAD_REQUEST.value()) {
			ErrorInfo errorInfo;
			try {
				try {
					SpringErrorResponse springErrorResponse = (SpringErrorResponse) decoder.decode(response,
							SpringErrorResponse.class);
					return new StartupException(springErrorResponse.getMessage());

				} catch (Exception e) {
					errorInfo = (ErrorInfo) decoder.decode(response, ErrorInfo.class);
					return new StartupException(errorInfo.getErrorMessage());

				}

			} catch (FeignException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return null;
	}

}
