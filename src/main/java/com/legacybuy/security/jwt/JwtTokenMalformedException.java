package com.legacybuy.security.jwt;

import org.springframework.security.core.AuthenticationException;

/**
 * Thrown when token cannot be parsed
 * 
 */
public class JwtTokenMalformedException extends AuthenticationException {

	private static final long serialVersionUID = -9028554973263072340L;

	public JwtTokenMalformedException(String msg) {
		super(msg);
	}
}
