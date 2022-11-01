package com.github.leandropa.springjava11.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.github.leandropa.springjava11.entity.User;

import java.util.Date;

public class JwtTokenUtil {

	public String generateAccessToken(User user) {
		return JWT.create()
				.withSubject(user.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
				.sign(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()));
	}

	public boolean validate(String token) {
		try {
			JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()))
					.build()
					.verify(token);
			return true;
		} catch (Exception ignored) {
		}
		return false;
	}

	public String getUsername(String token) {
		return JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()))
				.build()
				.verify(token)
				.getSubject();
	}
}
