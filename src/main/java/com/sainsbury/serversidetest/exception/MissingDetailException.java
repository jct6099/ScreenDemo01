package com.sainsbury.serversidetest.exception;

public class MissingDetailException extends RuntimeException {

	private static final long serialVersionUID = -7032467326764264073L;

	public MissingDetailException(String message) {
		super(message);
	}

}
