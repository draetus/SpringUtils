package com.mauricio.springsecurity.exception.unavailable;

import java.util.ResourceBundle;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ServiceUnavailableException extends ResponseStatusException{

	private static final long serialVersionUID = 2588499330842370136L;
	
	public ServiceUnavailableException() {
		super(HttpStatus.SERVICE_UNAVAILABLE, ResourceBundle.getBundle("messages/exception_messages").getString("unavailable.service"));
	}

}
