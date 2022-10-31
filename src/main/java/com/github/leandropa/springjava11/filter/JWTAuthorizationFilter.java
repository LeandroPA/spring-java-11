package com.github.leandropa.springjava11.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.github.leandropa.springjava11.util.SecurityConstants;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

	public JWTAuthorizationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

		String header = Optional
				.ofNullable(request.getHeader(SecurityConstants.HEADER_STRING))
				.orElse("");

		if (header.startsWith(SecurityConstants.TOKEN_PREFIX)) {

			UsernamePasswordAuthenticationToken auth = getAuthentication(header.substring(0, SecurityConstants.TOKEN_PREFIX.length()));

			SecurityContextHolder.getContext().setAuthentication(auth);

			return;
		}

		chain.doFilter(request, response);
	}

	private UsernamePasswordAuthenticationToken getAuthentication(String token) {
		return Optional.ofNullable(token)
				.map(token1 -> JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()))
						.build()
						.verify(token1)
						.getSubject())
				.map(user -> new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>()))
				.orElse(null); //TODO: throw an exception for invalid token
	}
}
