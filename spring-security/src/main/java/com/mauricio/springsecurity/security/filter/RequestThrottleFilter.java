package com.mauricio.springsecurity.security.filter;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.springframework.http.HttpStatus;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mauricio.springsecurity.component.utils.ServletUtils;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RequestThrottleFilter implements Filter {
	
	private int MAX_REQUESTS_PER_SECOND = 24;
	
	private LoadingCache<String, Integer> requestCountsPerIpAddress;
	
	public RequestThrottleFilter(){
        super();
        requestCountsPerIpAddress = CacheBuilder.newBuilder().
                expireAfterWrite(1, TimeUnit.SECONDS).build(new CacheLoader<String, Integer>() {
            public Integer load(String key) {
                return 0;
            }
        });
    }
	
	@Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }
	
	@Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        String clientIpAddress = ServletUtils.getClientIP((HttpServletRequest) servletRequest);
        
        if(isMaximumRequestsPerSecondExceeded(clientIpAddress)){
          httpServletResponse.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
          httpServletResponse.getWriter().write("Limite de requisições por segundo atingida");
          return;
         }

        filterChain.doFilter(servletRequest, servletResponse);
    }
	
	private boolean isMaximumRequestsPerSecondExceeded(String clientIpAddress){
        int requests = 0;
        try {
            requests = requestCountsPerIpAddress.get(clientIpAddress);
            if(requests >= MAX_REQUESTS_PER_SECOND){
                requestCountsPerIpAddress.put(clientIpAddress, requests);
                return true;
             }
        } catch (ExecutionException e) {
            requests = 0;
        }
        requests++;
        requestCountsPerIpAddress.put(clientIpAddress, requests);
        
        return false;
    }
	
	@Override
    public void destroy() {

    }

}
