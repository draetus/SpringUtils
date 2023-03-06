package com.mauricio.springsecurity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mauricio.springsecurity.model.Login;
import com.mauricio.springsecurity.repository.LoginRepository;

@Service
public class LoginService {
	
	@Autowired
	private LoginRepository loginRepository;
	
	public Login findByUsername(String username) {
		return loginRepository.findByUsername(username);
	}

}
