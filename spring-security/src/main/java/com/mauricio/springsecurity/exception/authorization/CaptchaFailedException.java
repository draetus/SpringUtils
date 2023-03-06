package com.mauricio.springsecurity.exception.authorization;

import java.util.ResourceBundle;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CaptchaFailedException extends ResponseStatusException{

	private static final long serialVersionUID = 7923344463829174438L;
	
	public CaptchaFailedException() {
		super(HttpStatus.UNAUTHORIZED, ResourceBundle.getBundle("messages/exception_messages").getString("authorization.captcha.failed"));
	}

}
