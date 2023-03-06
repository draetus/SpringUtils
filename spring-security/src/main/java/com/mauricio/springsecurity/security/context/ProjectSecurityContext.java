package com.mauricio.springsecurity.security.context;

import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.mauricio.springsecurity.security.model.DeepenUserDetails;
import com.mauricio.springsecurity.security.service.TokenAuthenticationService;

public abstract class ProjectSecurityContext {
	
	public static final String ROLE_USER = "ROLE_USER";
	public static final String ROLE_ADMIN = "ROLE_ADMIN";
	public static final String ROLE_BEWORK = "ROLE_BEWORK";
	public static final String ROLE_BECARE = "ROLE_BECARE";
	public static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS_PAY";
	
	public static final Long ROLE_USER_ID = 1L;
	public static final Long ROLE_ADMIN_ID = 2L;
	
	private ProjectSecurityContext() {
		throw new IllegalStateException("Utility class");
	}
	
	private static DeepenUserDetails getBaseAuthentication() {
		return (DeepenUserDetails)SecurityContextHolder.getContext().getAuthentication();
	}
	
	public static Long getUserId() {
		DeepenUserDetails authentication = getBaseAuthentication();
		return authentication.getExtraDataValueAsLong(TokenAuthenticationService.USER_ID);
	}
	
	public static Boolean isAdmin() {
		DeepenUserDetails authentication = getBaseAuthentication(); 
		return authentication.getAuthorities().stream()
				.map((GrantedAuthority role) -> role.toString())
				.collect(Collectors.toList())
				.contains(ROLE_ADMIN);
	}

}
