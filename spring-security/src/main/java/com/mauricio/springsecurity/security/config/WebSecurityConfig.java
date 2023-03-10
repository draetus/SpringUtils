package com.mauricio.springsecurity.security.config;

import java.util.Arrays;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.mauricio.springsecurity.external.captcha.service.CaptchaService;
import com.mauricio.springsecurity.security.service.TokenAuthenticationService;


@Configuration
@EnableWebSecurity
@EnableAutoConfiguration
@Profile(value = {"qa", "dev, prod, default"})
public abstract class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private DataSource dataSource;

	@Autowired
	@Lazy
	protected void configAuthentication(AuthenticationManagerBuilder auth, PasswordEncoder passwordEncoder,
			@Autowired UserDetailsService userDetailsService)
			throws Exception {
		auth.userDetailsService(userDetailsService);
		auth.jdbcAuthentication().dataSource(dataSource)
				.usersByUsernameQuery(usersByUsernameQuery())
				.authoritiesByUsernameQuery(authoritiesByUsernameQuery())
				.passwordEncoder(passwordEncoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		httpRoutesConfig(http);
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("admin").password("admin").roles("USER");
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("*"));
		configuration.setAllowedMethods(Arrays.asList(HttpMethod.GET.name(),HttpMethod.PUT.name(), HttpMethod.POST.name(), HttpMethod.DELETE.name(), HttpMethod.PATCH.name(), HttpMethod.OPTIONS.name()) );
		configuration.setAllowedHeaders(Arrays.asList(TokenAuthenticationService.HEADER_STRING, CaptchaService.HEADER_STRING, "Cache-Control", "Content-Type"));
		configuration.addExposedHeader(TokenAuthenticationService.HEADER_STRING);
		configuration.addExposedHeader(TokenAuthenticationService.HEADER_REFRESH_STRING);
		configuration.setAllowCredentials(false);
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);

		return source;
	}
	
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
	    return super.authenticationManagerBean();
	}
	
	protected abstract String usersByUsernameQuery();
	protected abstract String authoritiesByUsernameQuery();
	protected abstract void httpRoutesConfig(HttpSecurity http) throws Exception;

}
