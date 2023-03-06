package com.mauricio.springsecurity.security.filter;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.mauricio.springsecurity.security.exception.AuthException;
import com.mauricio.springsecurity.security.service.TokenAuthenticationService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class JWTAuthenticationFilter extends GenericFilterBean {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		try {
			Authentication authentication = TokenAuthenticationService.getAuthentication((HttpServletRequest) request);
			
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
			if (authentication != null) {
				TokenAuthenticationService.addAuthentication((HttpServletResponse) response, authentication, false);
			} 
			filterChain.doFilter(request, response);
		} catch(AuthException e) {
			setUnauthorizedResponse((HttpServletResponse) response, e.getMessage());
		}
	}
	
	private void setUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
		response.sendError(HttpStatus.UNAUTHORIZED.value(), message);
	}

}