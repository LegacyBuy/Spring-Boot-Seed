package com.legacybuy.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.legacybuy.utils.ResponseUtil;

@ControllerAdvice
@RestController
public class ExceptionHandlerController implements ErrorController {

	public static final String ERROR_MAPPING = "/error";
	public static final String ERROR_MESSAGE = "javax.servlet.error.message";
	public static final String ERROR_STATUS = "javax.servlet.error.status_code";

	@ExceptionHandler(value = Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public void handleException(HttpServletRequest request, HttpServletResponse response, Exception e) {
		ResponseUtil.respondFailure(response, HttpStatus.INTERNAL_SERVER_ERROR, e);
	}

	@RequestMapping(value = ERROR_MAPPING)
	public void error(HttpServletRequest request, HttpServletResponse response) {
		RequestAttributes requestAttributes = new ServletRequestAttributes(request);
		String message = requestAttributes.getAttribute(ERROR_MESSAGE, 0).toString();
		HttpStatus status = HttpStatus
				.valueOf((int) Integer.parseInt(requestAttributes.getAttribute(ERROR_STATUS, 0).toString()));
		ResponseUtil.respondFailure(response, status, message);
	}

	@Override
	public String getErrorPath() {
		return ERROR_MAPPING;
	}
}
