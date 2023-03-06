package com.mauricio.springsecurity.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mauricio.springsecurity.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{
	
	public User findByEmail(String email);

}
