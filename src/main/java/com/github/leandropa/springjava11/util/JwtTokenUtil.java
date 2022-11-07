package com.github.leandropa.springjava11.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.leandropa.springjava11.model.JwtResponse;

import java.util.Date;

public class JwtTokenUtil {

	public JwtResponse generateAccessToken(String username) {

		Date expiresAt = new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME);
		String token = JWT.create()
				.withSubject(username)
				.withExpiresAt(expiresAt)
				.sign(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()));

		return new JwtResponse(username, expiresAt, token);
	}

	public JwtResponse getAccessToken(String token) {

		try {
			DecodedJWT decodedJWT =  JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()))
					.build()
					.verify(token);

			return new JwtResponse(decodedJWT.getSubject(), decodedJWT.getExpiresAt(), token);
		} catch (JWTVerificationException e) {
			return null;
		}
	}
}
