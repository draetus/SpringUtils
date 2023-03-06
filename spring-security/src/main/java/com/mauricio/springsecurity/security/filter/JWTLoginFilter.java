package com.mauricio.springsecurity.security.filter;

import static com.mauricio.springsecurity.external.captcha.service.CaptchaService.LOGIN_ACTION;

import java.io.IOException;
import java.util.Collections;
import java.util.Hashtable;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mauricio.springsecurity.external.captcha.service.CaptchaService;
import com.mauricio.springsecurity.security.model.AccountCredentials;
import com.mauricio.springsecurity.security.model.DeepenUserDetails;
import com.mauricio.springsecurity.security.service.TokenAuthenticationService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {
	
	private CaptchaService captchaService;
	
	public JWTLoginFilter(String url, AuthenticationManager authManager, AuthenticationFailureHandler failureHandler, CaptchaService captchaService) {
		super(new AntPathRequestMatcher(url));
		setAuthenticationManager(authManager);
		setAuthenticationFailureHandler(failureHandler);
		this.captchaService = captchaService; 
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		captchaService.validateRequest(request, LOGIN_ACTION);
		
		AccountCredentials credentials = new ObjectMapper().readValue(request.getInputStream(),AccountCredentials.class);
		
		Hashtable<String, Object> extraData = new Hashtable<String, Object>();
		
		return getAuthenticationManager().authenticate(new DeepenUserDetails(
				credentials.getUsername(), credentials.getPassword(), Collections.emptyList(), extraData));
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain, Authentication auth) throws IOException, ServletException {
		TokenAuthenticationService.addAuthentication(response, auth, true);
	}

}
