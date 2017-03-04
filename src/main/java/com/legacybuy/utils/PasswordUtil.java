package com.legacybuy.utils;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.legacybuy.Application;

@Component
public class PasswordUtil {

	private static PasswordEncoder passwordEncoder;

	public static String encode(String password) {
		if (passwordEncoder == null) {
			passwordEncoder = Application.ctx.getBean(PasswordEncoder.class);
		}
		return passwordEncoder.encode(password);
	}
}
