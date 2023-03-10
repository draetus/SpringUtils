package com.mauricio.springsecurity.security.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mauricio.springsecurity.security.model.AccountCredentials;

@RestController
@RequestMapping("/login")
public class LoginController {
	
	@RequestMapping(method=RequestMethod.POST, value="", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> login(@RequestBody AccountCredentials accountCredentials){
		
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
