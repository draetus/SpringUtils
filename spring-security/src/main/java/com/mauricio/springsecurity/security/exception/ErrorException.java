package com.mauricio.springsecurity.security.exception;

public class ErrorException extends ProjectException{

	private static final long serialVersionUID = 1L;

	public ErrorException(String message, Throwable e) {
		super(message, e);
	}
	
}
