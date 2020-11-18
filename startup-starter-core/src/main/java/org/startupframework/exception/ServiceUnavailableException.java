package org.startupframework.exception;

public class ServiceUnavailableException extends StartupException {

	private static final long serialVersionUID = -2022445089428506234L;

	public ServiceUnavailableException() {

	}

	public ServiceUnavailableException(String message) {
		super(message);
	}

	public ServiceUnavailableException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public ServiceUnavailableException(String message, Throwable cause) {
		super(message, cause);
	}

}
