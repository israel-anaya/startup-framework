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
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
/*
class BaaSResponseErrorHandler extends DefaultResponseErrorHandler {

	List<HttpMessageConverter<?>> messageConverters;

	public BaaSResponseErrorHandler() {
		this.messageConverters = new ArrayList<>(new RestTemplate().getMessageConverters());
		this.messageConverters.add(new FormOAuth2AccessTokenMessageConverter());
		this.messageConverters.add(new FormOAuth2ExceptionHttpMessageConverter());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handleError(ClientHttpResponse response) throws IOException {
		for (HttpMessageConverter<?> converter : messageConverters) {
			if (converter.canRead(OAuth2Exception.class, response.getHeaders().getContentType())) {
				OAuth2Exception ex;
				try {
					ex = ((HttpMessageConverter<OAuth2Exception>) converter).read(OAuth2Exception.class, response);
				} catch (Exception e) {
					// ignore
					continue;
				}
				throw ex;
			}
		}
		super.handleError(response);
	}
}*/