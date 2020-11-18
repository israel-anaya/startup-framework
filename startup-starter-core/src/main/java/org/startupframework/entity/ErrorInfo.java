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

package org.startupframework.entity;

import org.startupframework.exception.StartupErrorCode;
import org.startupframework.exception.StartupException;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;
import java.util.Date;

import org.startupframework.exception.DataException;

import lombok.Data;

/**
 *
 * @author Arq. Jesús Israel Anaya Salazar
 */
public @Data class ErrorInfo {

	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	Date timestamp;	
	String exception;
	String errorCode;
	String errorMessage;

	public ErrorInfo() {
		timestamp = new Date(Instant.now().toEpochMilli());
	}

	public ErrorInfo(Exception ex) {
		this();
		this.exception = ex.getClass().getName();
		this.errorMessage = ex.getMessage();
	}

	public ErrorInfo(StartupException ex) {
		this();
		this.exception = ex.getClass().getName();
		//this.errorCode = StartupErrorCode.getErrorCode(ex);
		this.errorMessage = ex.getMessage();
	}

	public ErrorInfo(DataException ex) {
		this();
		this.exception = ex.getClass().getName();
		this.errorCode = StartupErrorCode.getErrorCode(ex);
		this.errorMessage = ex.getMessage();
	}
	

}
