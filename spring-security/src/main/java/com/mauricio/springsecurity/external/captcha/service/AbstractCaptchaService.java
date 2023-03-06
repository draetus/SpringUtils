package com.mauricio.springsecurity.external.captcha.service;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestOperations;

import com.mauricio.springsecurity.exception.authorization.ExceededMaxRequestException;
import com.mauricio.springsecurity.exception.authorization.InvalidCharacterTokenException;
import com.mauricio.springsecurity.external.captcha.config.CaptchaSettings;

import jakarta.servlet.http.HttpServletRequest;

public abstract class AbstractCaptchaService {
	
	@Autowired
    protected HttpServletRequest request;

    @Autowired
    protected CaptchaSettings captchaSettings;

    @Autowired
    protected ReCaptchaAttemptService reCaptchaAttemptService;

    @Autowired
    protected RestOperations restTemplate;
    
    protected static final Pattern RESPONSE_PATTERN = Pattern.compile("[A-Za-z0-9_-]+");
    
    protected static final String RECAPTCHA_URL_TEMPLATE = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s&remoteip=%s";
    
    public String getReCaptchaSite() {
        return captchaSettings.getSite();
    }

    public String getReCaptchaSecret() {
        return captchaSettings.getSecret();
    }
  

    protected void securityCheck(final String response) {
        if (reCaptchaAttemptService.isBlocked(getClientIP())) {
            throw new ExceededMaxRequestException();
        }

        if (!responseSanityCheck(response)) {
            throw new InvalidCharacterTokenException();
        }
    }

    protected boolean responseSanityCheck(final String response) {
        return StringUtils.hasLength(response) && RESPONSE_PATTERN.matcher(response).matches();
    }

    protected String getClientIP() {
        final String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

}
