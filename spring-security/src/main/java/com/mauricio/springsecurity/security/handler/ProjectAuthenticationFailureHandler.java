package com.mauricio.springsecurity.security.handler;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mauricio.springsecurity.security.exception.ResponseLog;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ProjectAuthenticationFailureHandler implements AuthenticationFailureHandler {


	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		
		response.setContentType("application/json;charset=UTF-8");
		String responseMessage = null;
		ResponseEntity<ResponseLog> feedback = null;
		HttpStatus responseStatus = HttpStatus.UNAUTHORIZED;

		responseMessage = getMessageByStatus(responseStatus);
		feedback = new ResponseEntity<>(new ResponseLog(responseMessage), responseStatus);

		ObjectMapper mapper = new ObjectMapper();
		response.getOutputStream().write(mapper.writeValueAsBytes(feedback));
		response.setStatus(responseStatus.value());

	}

	private String getMessageByStatus(HttpStatus status) {
		return status.toString();
	}

}
