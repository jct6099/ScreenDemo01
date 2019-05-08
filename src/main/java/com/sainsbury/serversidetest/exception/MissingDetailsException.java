package com.sainsbury.serversidetest.exception;

/**
 * Custom exception for missing product details
 * 
 * @author szeto
 */
public class MissingDetailsException extends RuntimeException {

	private static final long serialVersionUID = -7032467326764264073L;

	public MissingDetailsException(String message) {
		super(message);
	}

}
