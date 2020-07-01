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

package org.startupframework.service;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.startupframework.entity.Identifiable;
import org.startupframework.exception.DataException;

/**
* Validation
* 
* @author Arq. Jesús Israel Anaya Salazar
*/
abstract public class ObjectValidatorService<T extends Identifiable<String>> {
	
	protected ObjectValidatorService() {
	}

	@Autowired
	Validator validator;

	protected final void validateObjectConstraints(T obj) {
		// Valida a nivel de modelo utilizando las anotaciones del paquete:
		// javax.validation.constraints
		//
		// https://www.baeldung.com/javax-validation
		//
		Set<ConstraintViolation<T>> constraintViolations = validator.validate(obj);
		if (constraintViolations.size() > 0) {
			StringBuilder msg = new StringBuilder();
			msg.append("Validation error(s): ");

			for (ConstraintViolation<T> constraintViolation : constraintViolations) {

				String type = constraintViolation.getRootBean().getClass().getSimpleName();
				Path propertyName = constraintViolation.getPropertyPath();
				String error = constraintViolation.getMessage();
				Object invalidValue = constraintViolation.getInvalidValue();
				msg.append(String.format("%s.%s[%s valor=%s], ", type, propertyName.toString(), error, invalidValue));
			}
			throw new DataException(msg.toString());
		}
	}

	abstract protected void onValidateObject(T obj);

}
