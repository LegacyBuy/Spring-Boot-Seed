package com.legacybuy.security.jwt;

import org.springframework.security.core.AuthenticationException;

/**
 * Thrown when token cannot be found in the request header
 * 
 */

public class JwtTokenMissingException extends AuthenticationException {

	private static final long serialVersionUID = -4306043645810144925L;

	public JwtTokenMissingException(String msg) {
		super(msg);
	}
}
