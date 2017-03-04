package com.legacybuy.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

	@RequestMapping("/")
	String execute() {
		return "hello";
	}

	@RequestMapping("/exception")
	String exception() {
		int a = 10 / 0;
		System.out.println(a);
		return "hello";
	}
}
