package com.mauricio.springsecurity.security.service;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.mauricio.springsecurity.model.Role;
import com.mauricio.springsecurity.model.User;
import com.mauricio.springsecurity.security.context.ProjectSecurityContext;

@Service
public class AuthoritiesService {
	
	public void addAuthorities(List<GrantedAuthority> authorities, Role role, User user) {
		if (role.getId().equals(ProjectSecurityContext.ROLE_USER_ID)) {
			addUserAuthorities(authorities, user);
		}
		
		if (role.getId().equals(ProjectSecurityContext.ROLE_ADMIN_ID)) {
			addAdminAuthorities(authorities);
		}
		
	}
	
	private void addUserAuthorities(List<GrantedAuthority> authorities, User user) {
		authorities.add(new SimpleGrantedAuthority(ProjectSecurityContext.ROLE_USER));
	}
	
	private void addAdminAuthorities(List<GrantedAuthority> authorities) {
		authorities.add(new SimpleGrantedAuthority(ProjectSecurityContext.ROLE_USER));
		authorities.add(new SimpleGrantedAuthority(ProjectSecurityContext.ROLE_ADMIN));
		authorities.add(new SimpleGrantedAuthority(ProjectSecurityContext.ROLE_BEWORK));
	}

}
