package com.legacybuy.utils;

import java.util.Arrays;
import java.util.List;

import com.legacybuy.model.Authority;

public class AuthorityUtil {

	private AuthorityUtil() {
	}

	public static List<Authority> getAsList(Authority... authorities) {
		return Arrays.asList(authorities);
	}

}
