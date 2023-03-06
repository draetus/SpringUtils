package com.mauricio.springsecurity.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mauricio.springsecurity.model.User;
import com.mauricio.springsecurity.repository.UserRepository;
import com.mauricio.springsecurity.security.context.ProjectSecurityContext;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	public User findContextUser() {
		Optional<User> user = userRepository.findById(ProjectSecurityContext.getUserId());
		return user.isPresent() ? user.get() : null;
	}

}
