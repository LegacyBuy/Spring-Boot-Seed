package com.legacybuy.utils;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ResponseUtil {

	private static final String SUCCESS = "success";
	private static final String DATA = "data";
	private static final String MESSAGE = "message";
	private static final String TIMESTAMP = "timeStamp";
	private static final String EXCEPTION_CLASS = "class";

	private ResponseUtil() {
	}

	public static Map<String, Object> success(String message) {
		return success(null, message);
	}

	public static Map<String, Object> success(Object data) {
		return success(data, null);
	}

	public static Map<String, Object> success(Object data, String message) {
		Map<String, Object> map = new HashMap<String, Object>();
		setStatus(map, true);
		setData(map, data);
		setMessage(map, message);
		setTimeStamp(map);
		return map;
	}

	public static void respondSuccess(HttpServletResponse response, String message) {
		respondSuccess(response, null, message);
	}

	public static void respondSuccess(HttpServletResponse response, Object data) {
		respondSuccess(response, data, null);
	}

	public static void respondSuccess(HttpServletResponse response, Object data, String message) {
		Object obj = success(data, message);

		ObjectMapper objectMapper = new ObjectMapper();
		try {
			response.addHeader("Content-Type", "application/json;charset=UTF-8");
			response.getOutputStream().write(objectMapper.writeValueAsBytes(obj));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Map<String, Object> failure(Exception exception) {
		return failure(exception, null);
	}

	public static Map<String, Object> failure(String errorMessage) {
		return failure(null, errorMessage);
	}

	public static Map<String, Object> failure(Exception exception, String errorMessage) {
		Map<String, Object> map = new HashMap<String, Object>();
		setStatus(map, false);
		if (StringUtils.isEmpty(errorMessage) && exception != null) {
			errorMessage = exception.getLocalizedMessage();
		}
		setException(map, exception);
		setTimeStamp(map);
		setMessage(map, errorMessage);
		return map;
	}

	public static void respondFailure(HttpServletResponse response, HttpStatus statusCode, Exception exception) {
		respondFailure(response, statusCode, exception, null);
	}

	public static void respondFailure(HttpServletResponse response, HttpStatus statusCode, String error) {
		respondFailure(response, statusCode, null, error);
	}

	public static void respondFailure(HttpServletResponse response, HttpStatus statusCode, Exception exception,
			String error) {
		Object obj = failure(exception, error);

		ObjectMapper objectMapper = new ObjectMapper();
		response.addHeader("Content-Type", "application/json;charset=UTF-8");
		response.setStatus(statusCode.value());
		try {
			response.getOutputStream().write(objectMapper.writeValueAsBytes(obj));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void setMessage(Map<String, Object> result, String message) {
		if (message != null) {
			result.put(MESSAGE, message);
		}
	}

	private static void setStatus(Map<String, Object> result, boolean status) {
		result.put(SUCCESS, status);
	}

	private static void setTimeStamp(Map<String, Object> result) {
		result.put(TIMESTAMP, new Timestamp(System.currentTimeMillis()));
	}

	private static void setData(Map<String, Object> result, Object data) {
		if (data != null) {
			result.put(DATA, data);
		}
	}

	private static void setException(Map<String, Object> result, Exception e) {
		if (e != null) {
			result.put(EXCEPTION_CLASS, e.getClass());
		}
	}

}
