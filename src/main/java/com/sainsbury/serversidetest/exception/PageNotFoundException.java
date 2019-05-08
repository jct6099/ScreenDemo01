package com.sainsbury.serversidetest.exception;

/**
 * Custom exception for page not found
 * 
 * @author szetop
 */
public class PageNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -7032467326764264073L;

	public PageNotFoundException(String message) {
		super(message);
	}

}
