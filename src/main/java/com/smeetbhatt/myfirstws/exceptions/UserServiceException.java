package com.smeetbhatt.myfirstws.exceptions;

public class UserServiceException extends RuntimeException {

	private static final long serialVersionUID = -8869658968964335083L;

	public UserServiceException(String message) {
		super(message);
	}

}
