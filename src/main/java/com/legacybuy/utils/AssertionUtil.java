package com.legacybuy.utils;

public class AssertionUtil {

	private AssertionUtil() {
	}

	public static void notNull(Object obj) {
		notNull(obj, "");
	}

	public static void notNull(Object obj, String message) {
		if (obj == null) {
			throw new NullPointerException(message);
		}
	}

}
