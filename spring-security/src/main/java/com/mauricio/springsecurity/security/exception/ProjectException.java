package com.mauricio.springsecurity.security.exception;

public abstract class ProjectException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	protected ProjectException() {
		super();
	}

	protected ProjectException(String message) {
		super(message);
	}

	protected ProjectException(String message, Throwable t) {
		super(message, t);
	}

}
