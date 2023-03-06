package com.mauricio.springsecurity.security.model;

import lombok.Getter;

public enum RoleEnum {

	ADMIN(1),
	ADMIN_COMPANY(2),
	COMPANY(3),
	USER(4),
	SHARE(11),
	ANONYMOUS_PAY(12);
		 
	@Getter private final Integer id;
		
	private RoleEnum(Integer id){
		this.id = id;
	} 
}
