package com.mauricio.springsecurity.exception.authorization;

import java.util.ResourceBundle;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidCharacterTokenException extends ResponseStatusException{

	private static final long serialVersionUID = 7491201330006046197L;
	
	public InvalidCharacterTokenException() {
		super(HttpStatus.UNAUTHORIZED, ResourceBundle.getBundle("messages/exception_messages").getString("authorization.invalidcharactertoken"));
	}

}
