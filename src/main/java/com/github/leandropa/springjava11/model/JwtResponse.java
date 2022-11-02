package com.github.leandropa.springjava11.model;

import java.io.Serializable;

public class JwtResponse implements Serializable {

	private final String token;

	public JwtResponse(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}
}
