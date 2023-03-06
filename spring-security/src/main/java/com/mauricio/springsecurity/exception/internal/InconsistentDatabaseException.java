package com.mauricio.springsecurity.exception.internal;

import java.util.ResourceBundle;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InconsistentDatabaseException extends ResponseStatusException{

	private static final long serialVersionUID = 2008170755932023187L;
	
	public InconsistentDatabaseException() {
		super(HttpStatus.INTERNAL_SERVER_ERROR, ResourceBundle.getBundle("messages/exception_messages").getString("internal.inconsistentdatabase"));
	}

}
