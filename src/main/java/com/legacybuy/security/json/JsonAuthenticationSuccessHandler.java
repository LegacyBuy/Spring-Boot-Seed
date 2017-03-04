package com.legacybuy.security.json;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.legacybuy.model.User;
import com.legacybuy.security.jwt.JwtTokenGenerator;
import com.legacybuy.utils.ResponseUtil;

public class JsonAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private JwtTokenGenerator generator;

	public JsonAuthenticationSuccessHandler(JwtTokenGenerator generator) {
		this.generator = generator;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) {
		Map<String, String> userLoginSuccessResponse = new HashMap<>();

		User authenticatedUser = (User) authentication.getPrincipal();
		String token = generator.generateToken(authenticatedUser);

		userLoginSuccessResponse.put("token", token);
		userLoginSuccessResponse.put("token_type", "bearer");
		ResponseUtil.respondSuccess(response, userLoginSuccessResponse);
	}

}
