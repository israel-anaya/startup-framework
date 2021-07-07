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

package org.startupframework.security;

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 *
 * @author Arq. Jesús Israel Anaya Salazar
 */
public abstract class StartupSecurityConfig extends WebSecurityConfigurerAdapter {

	protected abstract void customAntMatchers(CustomizerAntMatchers customizer);

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests(authorization -> {
			authorization.antMatchers("/actuator/**").permitAll();
			CustomizerAntMatchers customizerAntMatchers = new CustomizerAntMatchers(authorization);

			customAntMatchers(customizerAntMatchers);

			authorization.anyRequest().authenticated();

		}).oauth2ResourceServer(oauth2 -> {
			oauth2.authenticationEntryPoint(startupAuthenticationEntryPoint());
			oauth2.accessDeniedHandler(startupAccessDeniedHandler());
			oauth2.jwt();
		});
	}

	@Bean
	public JwtDecoder startupJwtDecoder(OAuth2ResourceServerProperties properties) {
		String jwkSetUri = properties.getJwt().getJwkSetUri();
		NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();

		// jwtDecoder.setClaimSetConverter(new OrganizationSubClaimAdapter());
		return jwtDecoder;
	}

	@Bean
	AuthenticationEntryPoint startupAuthenticationEntryPoint() {
		return new StartupAuthenticationEntryPoint();
	}

	@Bean
	AccessDeniedHandler startupAccessDeniedHandler() {
		return new StartupAccessDeniedHandler();
	}
}
