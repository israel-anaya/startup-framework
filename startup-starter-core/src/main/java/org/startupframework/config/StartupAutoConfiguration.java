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

package org.startupframework.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.startupframework.aspect.StartupControllerAspect;
import org.startupframework.log.HttpServiceLogger;
import org.startupframework.log.HttpServiceLoggerDefault;
import org.startupframework.validation.ObjectValidator;

/**
 * 
 *
 * @author Arq. Jesús Israel Anaya Salazar
 */
@Configuration
@EnableConfigurationProperties(StartupProperties.class)
public class StartupAutoConfiguration {

	@Autowired
	private StartupProperties properties;

	@Bean
	StartupControllerAspect startupControllerAspect() {
		return new StartupControllerAspect(properties, httpServiceLogger());
	}

	@Bean
	HttpServiceLogger httpServiceLogger() {
		return new HttpServiceLoggerDefault(properties);
	}

	@Bean
	ObjectValidator objectValidator() {
		return ObjectValidator.getInstance();
	}
}
