package com.legacybuy.security.social;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.NativeWebRequest;

import com.legacybuy.model.User;
import com.legacybuy.security.jwt.JwtTokenGenerator;
import com.legacybuy.service.UserService;
import com.legacybuy.utils.ResponseUtil;

@Service
public class SocialSignInAdapter implements SignInAdapter {

	@Autowired
	UserService userService;

	@Autowired
	JwtTokenGenerator generator;

	@Override
	public String signIn(String localUserId, Connection<?> connection, NativeWebRequest request) {
		System.out.println(" ====== Sign In adapter========localUserId:" + localUserId);

		Map<String, String> userLoginSuccessResponse = new HashMap<>();

		User authenticatedUser = userService.findUserByUsername(localUserId);
		String token = generator.generateToken(authenticatedUser);

		userLoginSuccessResponse.put("token", token);
		userLoginSuccessResponse.put("token_type", "bearer");
		ResponseUtil.respondSuccess(request.getNativeResponse(HttpServletResponse.class), userLoginSuccessResponse);
		return null;
	}
}
