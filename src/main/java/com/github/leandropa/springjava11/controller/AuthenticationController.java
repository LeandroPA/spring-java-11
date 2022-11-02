package com.github.leandropa.springjava11.controller;

import com.github.leandropa.springjava11.entity.User;
import com.github.leandropa.springjava11.model.JwtResponse;
import com.github.leandropa.springjava11.util.JwtTokenUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

	@Resource
	private JwtTokenUtil jwtTokenUtil;
	@Resource
	private AuthenticationManager authenticationManager;

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> auth(@RequestBody User user) throws Exception {

		Authentication auth = authenticate(user.getUsername(), user.getPassword());

		final String token = jwtTokenUtil.generateAccessToken(user.getUsername());

		return ResponseEntity.ok(new JwtResponse(token));
	}

	private Authentication authenticate(String username, String password) throws Exception {
		try {
			return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}

}
