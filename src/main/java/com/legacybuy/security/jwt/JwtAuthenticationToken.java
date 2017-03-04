package com.legacybuy.security.jwt;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

/**
 * Holder for JWT token from the request.
 * <p/>
 * Other fields aren't used but necessary to comply to the contracts of
 * AbstractUserDetailsAuthenticationProvider
 *
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class JwtAuthenticationToken extends UsernamePasswordAuthenticationToken {

	private static final long serialVersionUID = -3708486660391940867L;

	@Setter(AccessLevel.NONE)
	private String token;

	public JwtAuthenticationToken(String token) {
		super(null, null);
		this.token = token;
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return null;
	}
}