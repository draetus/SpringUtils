package com.mauricio.springsecurity.security.service;

import static com.mauricio.springsecurity.component.utils.ObjectUtils.isNotNull;
import static com.mauricio.springsecurity.component.utils.ObjectUtils.isNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mauricio.springsecurity.exception.internal.InconsistentDatabaseException;
import com.mauricio.springsecurity.model.Login;
import com.mauricio.springsecurity.model.User;
import com.mauricio.springsecurity.security.model.DeepenUserDetails;
import com.mauricio.springsecurity.service.LoginService;
import com.mauricio.springsecurity.service.UserService;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class AuthenticationService implements UserDetailsService {

	@Autowired
	private LoginService loginService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthoritiesService authoritiesService;

	@Override
	public DeepenUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Login login = this.loginService.findByUsername(username);
		User user = this.userService.findByEmail(username);
		Map<String, Object> extraData = new HashMap<>();
		List<GrantedAuthority> authorities = new ArrayList<>();

		if (isNull(login)) {
			throw new UsernameNotFoundException(username);
		}
		
		if (isNull(login.getRole())) {
			throw new InconsistentDatabaseException();
		}
		
		if (isNotNull(user)) {
			extraData.put(TokenAuthenticationService.USER_ID, user.getId());
			authoritiesService.addAuthorities(authorities, login.getRole(), user);
		}
		
		return new DeepenUserDetails(login.getUsername(), login.getPassword(), authorities, extraData);
	}

	
	public void renewUserAuthorities(HttpServletResponse response) {
		User user = userService.findContextUser();
		
		DeepenUserDetails deepenUserDetails = loadUserByUsername(user.getEmail());
		
		TokenAuthenticationService.addAuthentication(response, deepenUserDetails, false);
	}
}
