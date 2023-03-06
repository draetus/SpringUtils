package com.mauricio.springsecurity.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.mauricio.springsecurity.external.captcha.service.CaptchaService;
import com.mauricio.springsecurity.security.filter.JWTAuthenticationFilter;
import com.mauricio.springsecurity.security.filter.JWTLoginFilter;
import com.mauricio.springsecurity.security.filter.MaxRequestSizeFilter;
import com.mauricio.springsecurity.security.filter.RequestThrottleFilter;
import com.mauricio.springsecurity.security.handler.ProjectAuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
@EnableAutoConfiguration
@Profile(value = {"qa", "dev", "prod", "default"})
public class SecurityConfig extends WebSecurityConfig {
	
	@Autowired
	private CaptchaService captchaService;

	@Override
	protected String usersByUsernameQuery() {
		return "select username as username,password as password, true from public.login where username=? and deleted=false";
	}

	@Override
	protected String authoritiesByUsernameQuery() {
		return "select username as username, null as username from public.login where username=? and deleted=false";
	}

	@Override
	protected void httpRoutesConfig(HttpSecurity http) throws Exception {
		http.csrf().disable();

		http.cors().configurationSource(corsConfigurationSource()).and().authorizeRequests()
				// Authentication
				
				.antMatchers(HttpMethod.POST, "/login").permitAll()
				.antMatchers(HttpMethod.POST, "/refresh-token/access-token").permitAll()
				.antMatchers(HttpMethod.POST, "/user").permitAll()
				.antMatchers(HttpMethod.GET, "/user/exists/**").permitAll()
				.antMatchers(HttpMethod.POST, "/password/reset/request").permitAll()
				.antMatchers(HttpMethod.POST, "/password/reset/code").permitAll()
				.antMatchers(HttpMethod.GET, "/contract/file/bewiki/usecontract").permitAll()
				.antMatchers(HttpMethod.POST, "/geocode/postalcode/**").permitAll()
				.antMatchers(HttpMethod.GET, "/img/**").permitAll()
				.antMatchers(HttpMethod.OPTIONS).permitAll()
				
				.anyRequest().authenticated().and().httpBasic().disable()

				// Filter login requests
				.addFilterBefore(
						new JWTLoginFilter("/login", authenticationManager(), new ProjectAuthenticationFailureHandler(), captchaService),
						UsernamePasswordAuthenticationFilter.class)
				// Filter others requests to check JWT in header
				.addFilterBefore(new JWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(new RequestThrottleFilter(), JWTAuthenticationFilter.class)
				.addFilterBefore(new MaxRequestSizeFilter(), JWTAuthenticationFilter.class);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/v3/api-docs/**", "/swagger-ui.html", "/webjars/swagger-ui/**", "/swagger-resources/**", 
				"/configuration/security", "/configuration/ui","/webjars/**", "/csrf", "/swagger-ui/**");
	}
}
