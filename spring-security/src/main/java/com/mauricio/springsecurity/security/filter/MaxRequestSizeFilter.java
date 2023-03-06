package com.mauricio.springsecurity.security.filter;

import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import com.mauricio.springsecurity.exception.authorization.ExceededMaxRequestException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class MaxRequestSizeFilter extends OncePerRequestFilter {
	
	private final Long MAX_BYTES_PER_REQUEST = 700000000l;
	
	@Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response, FilterChain filterChain)
                    throws ServletException, IOException, ResponseStatusException {
        if (request.getContentLengthLong() > MAX_BYTES_PER_REQUEST) {
        	throw new ExceededMaxRequestException();
        }
        filterChain.doFilter(request, response);
    }

}
