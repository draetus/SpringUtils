package com.mauricio.springsecurity.security.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mauricio.springsecurity.security.exception.AuthException;
import com.mauricio.springsecurity.security.model.DeepenUserDetails;
import com.mauricio.springsecurity.security.model.RoleEnum;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class TokenAuthenticationService {

	public static final String HEADER_STRING = "Authorization";
	public static final String HEADER_REFRESH_STRING = "AuthorizationRefresh";
	
	public static final String USER_ID = "user_id";

	private static final String TOKEN_SIGN_SECRET = "0xffDeepenBewikiApplicationBackend8029";
	private static final int SESSION_MINUTES = 60;
	private static final int SESSION_RESET_PASSWORD_TIME_TYPE = Calendar.HOUR;
	private static final int SESSION_RESET_PASSWORD_TIME_VALUE = 4;

	private static final Algorithm TOKEN_SIGN_ALGORITHM = getTokenSignAlgorithm();
	private static final String TOKEN_ID = "id";
	private static final String USERNAME = "username";

	private static final String CLAIM_ROLES = "ROLES";
	private static final String ISSUER = "deepen.com.br";
	private static final String ROLE_PREFIX = "ROLE_";
	
	public enum Auth{
		LOGIN, AUTH, ANONYMOUS_PAY
	}

	private static Algorithm getTokenSignAlgorithm() {
		try {
			return Algorithm.HMAC256(TOKEN_SIGN_SECRET);
		} catch (IllegalArgumentException | UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public static void addAuthentication(HttpServletResponse response, Authentication auth, boolean login) {
		Auth authEnum;
		String[] roles = extractRolesAsArray(auth);
		if(login) {
			authEnum = Auth.LOGIN;
		} else {
			if(containsRole(RoleEnum.ANONYMOUS_PAY, Arrays.asList(roles))){
				authEnum = Auth.ANONYMOUS_PAY;
			} else {
				authEnum =  Auth.AUTH;
			}
		}

		String id = UUID.randomUUID().toString();
		String jwt = createDefaultSessionToken(id, auth, roles, authEnum);
		response.setHeader(HEADER_STRING, jwt);
	}

	public static String createAnonymousPaySessionToken(String tokenId) {
		return createToken(tokenId, null, new String[]{ROLE_PREFIX + RoleEnum.ANONYMOUS_PAY.name()}, Auth.ANONYMOUS_PAY);
	}

	private static String createDefaultSessionToken(String tokenId, Authentication auth, String[] roles, Auth authEnum) {
		return createToken(tokenId, auth, roles, authEnum);
	}

	private static String createToken(String tokenId, Authentication auth, String[] roles, Auth authEnum) {
		Calendar exp = Calendar.getInstance();
		exp.setTime(new Date());
		exp.add(Calendar.MINUTE, SESSION_MINUTES);
		
		String username = auth.getName();
		
		switch (authEnum) {
			case LOGIN: 
			return jwtLogin(tokenId, auth, exp.getTime(), roles);
			case AUTH: 
			return jwtAuth(tokenId, auth, exp.getTime(), roles);
			case ANONYMOUS_PAY:
			return jwtAnonymous(username, false, roles);
		}
		return null;
	}
	
	private static String jwtLogin(String tokenId, Authentication auth, Date exp, String[] roles) {
		String username = auth.getName();
		Map<String, Object> detais = ((DeepenUserDetails) auth.getPrincipal()).getExtraData();

		return JWT.create().withIssuer(ISSUER).withSubject(username)
				.withArrayClaim(CLAIM_ROLES, roles)
				.withClaim(TOKEN_ID, tokenId)		
				.withClaim(USER_ID, (Long) detais.get(USER_ID))
				.withExpiresAt(exp).sign(TOKEN_SIGN_ALGORITHM);
	}
	
	private static String jwtAuth(String tokenId, Authentication auth, Date exp, String[] roles) {
		String username = auth.getName();
		Map<String, Object> detais = ((DeepenUserDetails) auth).getExtraData();
		return JWT.create().withIssuer(ISSUER).withSubject(username)
				.withArrayClaim(CLAIM_ROLES, roles)
				.withClaim(TOKEN_ID, tokenId)	
				.withClaim(USER_ID, (Long) detais.get(USER_ID))
				.withClaim(USERNAME, username)
				.withExpiresAt(exp).sign(TOKEN_SIGN_ALGORITHM);	
	}
	
	private static String jwtAnonymous(String username, boolean hasExpiration, String[] roles) {
		if(!hasExpiration) {
			 return JWT.create().withIssuer(ISSUER).withSubject(username).withClaim(USERNAME, username)
				.withArrayClaim(CLAIM_ROLES, roles)
				.sign(TOKEN_SIGN_ALGORITHM);	
		} else {
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(SESSION_RESET_PASSWORD_TIME_TYPE, SESSION_RESET_PASSWORD_TIME_VALUE);
			
			return  JWT.create().withIssuer(ISSUER).withSubject(username).withClaim(USERNAME, username)
				.withArrayClaim(CLAIM_ROLES, roles)
				.withExpiresAt(c.getTime())
				.sign(TOKEN_SIGN_ALGORITHM);
		}
	}
	

	public static Authentication getAuthentication(HttpServletRequest request) {
		String token = request.getHeader(HEADER_STRING);
		if (token != null) {
			String[] tokenSplits = token.split(" ");
			if (tokenSplits[0].equals("Bearer")) {
				try {
					token = tokenSplits[1];

					return decodeJWT(token, false);
				} catch (TokenExpiredException e) {
					throw new AuthException("Token expirado, acesso negado.");
				} catch (JWTVerificationException e) {
					throw new AuthException("Acesso negado.");
				}
			}
		}
		return null;
	}

	private static DeepenUserDetails decodeJWT(String token, boolean acceptExpired) {
		JWTVerifier verifier = JWT.require(TOKEN_SIGN_ALGORITHM).withIssuer(ISSUER).acceptIssuedAt(acceptExpired ? Long.MAX_VALUE : 0).build();
		
		DecodedJWT jwt = verifier.verify(token);

		String user = jwt.getSubject();
		Claim claimRoles = jwt.getClaims().get(CLAIM_ROLES);
		
		if (claimRoles != null) {
			List<String> rolesString = claimRoles.asList(String.class);
			List<GrantedAuthority> authorities = stringToGrantedAuthorities(rolesString);
			boolean isAnonymous = containsRole(RoleEnum.ANONYMOUS_PAY, rolesString);
			Hashtable<String, Object> extraData = new Hashtable<String, Object>();
		
			if (user != null) {
				if(!isAnonymous) {
					Claim userClaim = jwt.getClaim(USER_ID);
					if (!userClaim.isNull()) {
						extraData.put(USER_ID, Long.valueOf(jwt.getClaim(USER_ID).asInt()));
					}
				}
				return new DeepenUserDetails(user, null, authorities, extraData);
			}
		}
		
		return null;
	}
	
	private static List<GrantedAuthority> stringToGrantedAuthorities(List<String> rolesString){
		List<GrantedAuthority> authorities = new ArrayList<>();
		for (String role : rolesString) {
			authorities.add(createRole(role));
		}
		return authorities;
	}
	
	private static boolean containsRole(RoleEnum role, List<String> roles) {
		return roles.contains(ROLE_PREFIX + role.name());
	}
	

	private static SimpleGrantedAuthority createRole(String role) {
		return new SimpleGrantedAuthority(role.startsWith(ROLE_PREFIX) ? role : ROLE_PREFIX + role);
	}
	
	private static String[] extractRolesAsArray(Authentication auth) {
		String[] roles = new String[auth.getAuthorities().size()];
		int index = 0;
		for (Iterator<?> iterator = auth.getAuthorities().iterator(); iterator.hasNext();) {
			GrantedAuthority authority = (GrantedAuthority) iterator.next();
			roles[index] = authority.getAuthority();
			index++;
		}
		return roles;
	}
	
	public static String createResetPasswordToken(String username) {
		return jwtAnonymous(username, true , new String[]{ROLE_PREFIX + RoleEnum.ANONYMOUS_PAY.name()});
	}
}
