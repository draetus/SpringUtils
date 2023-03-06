package com.mauricio.springsecurity.repository;

import org.springframework.data.repository.CrudRepository;

import com.mauricio.springsecurity.model.Login;

public interface LoginRepository extends CrudRepository<Login, Long>{
	
	public Login findByUsername(String username);

}
