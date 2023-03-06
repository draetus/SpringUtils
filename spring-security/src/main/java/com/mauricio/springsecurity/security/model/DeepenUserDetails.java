package com.mauricio.springsecurity.security.model;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class DeepenUserDetails extends UsernamePasswordAuthenticationToken implements UserDetails{

	private static final long serialVersionUID = 1L;
	
	private Map<String, Object> extraData;
	
	public DeepenUserDetails(Object principal, Object credentials,
			Collection<? extends GrantedAuthority> authorities,  Map<String, Object> extraData) {
		super(principal, credentials, authorities);
		this.extraData = extraData;		 
	}

	@Override
	public String getPassword() {
		return (String)getCredentials();
	}

	@Override
	public String getUsername() {
		return (String)getPrincipal();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
	public Integer getExtraDataValueAsInt(String key){
		return Integer.valueOf(getExtraDataValue(key));
	}
	
	public Long getExtraDataValueAsLong(String key){
		Object obj = getExtraDataObjectValue(key);
		Long value = null;
		if (obj instanceof Number){
			value = ((Number) obj).longValue(); 
		}else if (obj instanceof String) {
			value = Long.valueOf((String)obj);
		}else {
			throw new RuntimeException("Falha ao converter valor no contexto de segurança "+key);
		}
		return value;
	}
	
	public String getExtraDataValueAsString(String key){
		return getExtraDataValue(key);
	}
	
	private String getExtraDataValue(String key){
		return (String)this.extraData.get(key);
	}
	
	
	private Object getExtraDataObjectValue(String key){
		return this.extraData.get(key);
	}

}

