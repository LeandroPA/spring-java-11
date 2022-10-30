package com.github.leandropa.springjava11.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/")
public class IndexController {

	@RequestMapping()
	String index() {
		return "Hello World!";
	}
}
