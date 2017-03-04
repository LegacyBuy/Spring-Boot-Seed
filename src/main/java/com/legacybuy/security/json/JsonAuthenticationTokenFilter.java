package com.legacybuy.security.json;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.legacybuy.utils.Constants;

public class JsonAuthenticationTokenFilter extends AbstractAuthenticationProcessingFilter {

	private boolean postOnly = true;

	public static final String SPRING_SECURITY_FORM_USERNAME_KEY = "username";
	public static final String SPRING_SECURITY_FORM_PASSWORD_KEY = "password";

	public JsonAuthenticationTokenFilter() {
		super(new AntPathRequestMatcher(Constants.LOGIN_V1_URL, "POST"));
	}

	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException {
		if (postOnly && !request.getMethod().equals("POST")) {
			throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
		}

		InputStream ip = request.getInputStream();

		JSONObject json = new JSONObject(new JSONTokener(ip));

		String username = getValue(json, SPRING_SECURITY_FORM_USERNAME_KEY);
		String password = getValue(json, SPRING_SECURITY_FORM_PASSWORD_KEY);

		if (username == null) {
			username = "";
		}

		if (password == null) {
			password = "";
		}

		username = username.trim();

		JsonAuthenticationToken authRequest = new JsonAuthenticationToken(username, password);
		setDetails(request, authRequest);

		return this.getAuthenticationManager().authenticate(authRequest);
	}

	private String getValue(JSONObject json, String property) {
		try {
			return json.getString(property);
		} catch (Exception e) {
			return null;
		}
	}

	protected void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
		authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
	}

}