package com.legacybuy.security.json;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class JsonAuthenticationToken extends UsernamePasswordAuthenticationToken {

	private static final long serialVersionUID = -3708486660391940867L;

	public JsonAuthenticationToken(Object principal, Object credentials) {
		super(principal, credentials);
	}

	public JsonAuthenticationToken(Object principal, Object credentials,
			Collection<? extends GrantedAuthority> authorities) {
		super(principal, credentials, authorities);
	}

}