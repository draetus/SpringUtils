package com.mauricio.springsecurity.component.utils;

import jakarta.servlet.http.HttpServletRequest;

public abstract class ServletUtils {
	
	public static String HOME_IP = "127.0.0.1";
	
	public static String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null){
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

}
