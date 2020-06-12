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

package org.startupframework;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.startupframework.exception.DataException;

/*
public final class DataExceptionHttpMessageConverter implements HttpMessageConverter<DataException> {

	private static final List<MediaType> SUPPORTED_MEDIA = Collections.singletonList(MediaType.APPLICATION_JSON);

	private final FormHttpMessageConverter delegateMessageConverter = new FormHttpMessageConverter();

	public boolean canRead(Class<?> clazz, MediaType mediaType) {
		return DataException.class.equals(clazz) && MediaType.APPLICATION_JSON.equals(mediaType);
	}

	public boolean canWrite(Class<?> clazz, MediaType mediaType) {
		return DataException.class.equals(clazz) && MediaType.APPLICATION_JSON.equals(mediaType);
	}

	public List<MediaType> getSupportedMediaTypes() {
		return SUPPORTED_MEDIA;
	}

	public DataException read(Class<? extends DataException> clazz, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {
		MultiValueMap<String, String> data = delegateMessageConverter.read(null, inputMessage);
		Map<String,String> flattenedData = data.toSingleValueMap();
		return DataException.valueOf(flattenedData);
	}

	public void write(DataException t, MediaType contentType, HttpOutputMessage outputMessage) throws IOException,
			HttpMessageNotWritableException {
		MultiValueMap<String, String> data = new LinkedMultiValueMap<String, String>();
		data.add(OAuth2Exception.ERROR, t.getOAuth2ErrorCode());
		data.add(OAuth2Exception.DESCRIPTION, t.getMessage());
		Map<String, String> additionalInformation = t.getAdditionalInformation();
		if(additionalInformation != null) {
			for(Map.Entry<String,String> entry : additionalInformation.entrySet()) {
				data.add(entry.getKey(), entry.getValue());
			}
		}
		delegateMessageConverter.write(data, contentType, outputMessage);
	}



}
*/