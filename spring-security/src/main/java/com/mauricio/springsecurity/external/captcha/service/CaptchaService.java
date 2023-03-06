package com.mauricio.springsecurity.external.captcha.service;

import static com.mauricio.springsecurity.component.utils.ObjectUtils.not;

import java.net.URI;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import com.mauricio.springsecurity.exception.authorization.CaptchaFailedException;
import com.mauricio.springsecurity.exception.unavailable.ServiceUnavailableException;
import com.mauricio.springsecurity.external.captcha.dto.GoogleResponse;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class CaptchaService extends AbstractCaptchaService {
	
	public static final String HEADER_STRING = "g-recaptcha-response";
	public static final String REGISTER_ACTION = "register";
	public static final String LOGIN_ACTION = "login";
	public static final String RESET_PASSWORD_ACTION = "resetPassword";
	
	public void validateRequest(HttpServletRequest request, String action) {
		String response = request.getHeader(HEADER_STRING);
		// Opcional caso app não tenha recaptcha
		if (response == null) {
			return;
		}
		
		processResponse(response, REGISTER_ACTION);
	}

    private void processResponse(final String response, String action) {
        securityCheck(response);

        final URI verifyUri = URI.create(String.format(RECAPTCHA_URL_TEMPLATE, getReCaptchaSecret(), response, getClientIP()));
        try {
            final GoogleResponse googleResponse = restTemplate.getForObject(verifyUri, GoogleResponse.class);

            if (!googleResponse.isSuccess()) {
                if (        googleResponse.hasClientError() 
                		|| not(googleResponse.getAction().equals(action))
                		|| googleResponse.getScore() < captchaSettings.getThreshold()) {
                    reCaptchaAttemptService.reCaptchaFailed(getClientIP());
                }
                throw new CaptchaFailedException();
            }
        } catch (RestClientException rce) {
            throw new ServiceUnavailableException();
        }
        reCaptchaAttemptService.reCaptchaSucceeded(getClientIP());
    }

}
