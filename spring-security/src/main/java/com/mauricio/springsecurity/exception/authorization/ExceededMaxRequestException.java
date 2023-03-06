package com.mauricio.springsecurity.exception.authorization;

import java.util.ResourceBundle;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ExceededMaxRequestException extends ResponseStatusException {

	private static final long serialVersionUID = 7550792099275132298L;
	
	public ExceededMaxRequestException() {
		super(HttpStatus.TOO_MANY_REQUESTS, ResourceBundle.getBundle("messages/exception_messages").getString("authorization.exceededmaxrequest"));
	}

}
