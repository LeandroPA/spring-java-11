package com.github.leandropa.springjava11.filter;

import com.github.leandropa.springjava11.util.JwtTokenUtil;
import com.github.leandropa.springjava11.util.SecurityConstants;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class JWTAuthorizationFilter extends OncePerRequestFilter {

	@Resource
	private JwtTokenUtil jwtTokenUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		String header = Optional
				.ofNullable(request.getHeader(SecurityConstants.HEADER_STRING))
				.orElse("");

		if (header.startsWith(SecurityConstants.TOKEN_PREFIX)) {

			UsernamePasswordAuthenticationToken auth = getAuthentication(header.substring(SecurityConstants.TOKEN_PREFIX.length()));
			auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

			SecurityContextHolder.getContext().setAuthentication(auth);
		}

		chain.doFilter(request, response);
	}

	private UsernamePasswordAuthenticationToken getAuthentication(String token) {
		return Optional.ofNullable(token)
				.filter(token1 -> jwtTokenUtil.validate(token1))
				.map(token1 -> jwtTokenUtil.getUsername(token1))
				.map(user -> new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>()))
				.orElse(null); //TODO: throw an exception for invalid token?
	}
}
