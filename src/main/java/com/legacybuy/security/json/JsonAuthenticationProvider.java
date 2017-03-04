package com.legacybuy.security.json;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class JsonAuthenticationProvider extends DaoAuthenticationProvider {

	@Autowired
	public JsonAuthenticationProvider(UserDetailsService userDetailsService) {
		setUserDetailsService(userDetailsService);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (JsonAuthenticationToken.class.isAssignableFrom(authentication));
	}
}
