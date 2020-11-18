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

package org.startupframework.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.startupframework.entity.ErrorInfo;
import org.startupframework.exception.DataException;
import org.startupframework.exception.DataNotFoundException;
import org.startupframework.exception.DuplicateDataException;
import org.startupframework.exception.ServiceUnavailableException;
import org.startupframework.exception.StartupException;

/**
 * Base class for Controllers.
 *
 * @author Arq. Jesús Israel Anaya Salazar
 */
public abstract class StartupEndpoint {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorInfo> handleException(Exception ex) throws Exception {
		return new ResponseEntity<ErrorInfo>(new ErrorInfo(ex), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(StartupException.class)
	public ResponseEntity<ErrorInfo> handleException(StartupException ex) throws Exception {
		return new ResponseEntity<ErrorInfo>(new ErrorInfo(ex), HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(ServiceUnavailableException.class)
	public ResponseEntity<ErrorInfo> handleException(ServiceUnavailableException ex) throws Exception {
		return new ResponseEntity<ErrorInfo>(new ErrorInfo(ex), HttpStatus.SERVICE_UNAVAILABLE);
	}
	
	@ExceptionHandler(DataException.class)
	public ResponseEntity<ErrorInfo> handleException(DataException ex) throws Exception {
		return new ResponseEntity<ErrorInfo>(new ErrorInfo(ex), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(DuplicateDataException.class)
	public ResponseEntity<ErrorInfo> handleException(DuplicateDataException ex) throws Exception {
		return new ResponseEntity<ErrorInfo>(new ErrorInfo(ex), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(DataNotFoundException.class)
	public ResponseEntity<ErrorInfo> handleException(DataNotFoundException ex) throws Exception {
		return new ResponseEntity<ErrorInfo>(new ErrorInfo(ex), HttpStatus.NOT_FOUND);
	}
}
