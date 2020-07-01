package org.startupframework.feign;

import lombok.Data;

@Data
public class SpringErrorResponse {

	public SpringErrorResponse() {
	}

	String timestamp;
	String path;
	int status;
	String error;
	String message;
}