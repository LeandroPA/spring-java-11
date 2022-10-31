package com.github.leandropa.springjava11.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.leandropa.springjava11.entity.User;
import com.github.leandropa.springjava11.util.SecurityConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	@Resource
	private ObjectMapper objectMapper;

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);

		setFilterProcessesUrl(SecurityConstants.SIGN_UP_URL);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

		try {
			Optional<User> optionalUser = Optional.ofNullable(
					objectMapper.readValue(request.getInputStream(), User.class));

			User user = optionalUser.orElseThrow();

			return getAuthenticationManager().authenticate(
					new UsernamePasswordAuthenticationToken(
							user.getUsername(),
							user.getPassword(),
							new ArrayList<>()
					)
			);

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication auth) throws IOException, ServletException {

		String token = JWT.create()
				.withSubject(((User) auth.getPrincipal()).getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
				.sign(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()));

		String body = ((User) auth.getPrincipal()).getUsername() + " " + token;

		response.getWriter().write(body);
		response.getWriter().flush();
	}
}
