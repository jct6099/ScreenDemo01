package com.sainsbury.serversidetest.exception;

public class ServiceNotAvailableException extends RuntimeException {

	private static final long serialVersionUID = -7032467326764264073L;

	public ServiceNotAvailableException(String message) {
		super(message);
	}

}
