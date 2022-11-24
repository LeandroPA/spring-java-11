package com.github.leandropa.springjava11.controller;

import com.github.leandropa.springjava11.validation.UserAuthentication;
import com.github.leandropa.springjava11.entity.User;
import com.github.leandropa.springjava11.model.JwtResponse;
import com.github.leandropa.springjava11.util.JwtTokenUtil;
import com.github.leandropa.springjava11.util.SecurityConstants;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;

@Log
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

	@Resource
	private JwtTokenUtil jwtTokenUtil;
	@Resource
	private AuthenticationManager authenticationManager;

	@PostMapping
	public ResponseEntity<?> auth(@Validated(UserAuthentication.class) @RequestBody User user) throws Exception {

		log.info("Receiving authenticate request with " + user);

		Authentication auth = authenticate(user.getUsername(), user.getPassword());

		log.info("User " + user.getUsername() + " authenticated");

		final JwtResponse token = jwtTokenUtil.generateAccessToken(user.getUsername());

		log.info("Responding with generated token");

		return ResponseEntity.ok(token);
	}
	@GetMapping("/token")
	public ResponseEntity<?> tokenInfo(@RequestHeader(SecurityConstants.HEADER_STRING) String authHeader) {

		String token = authHeader.replaceFirst(SecurityConstants.TOKEN_PREFIX, "");

		JwtResponse tokenResponse = jwtTokenUtil.getAccessToken(token);

		log.info("Retrieving token info for user " + tokenResponse.getUsername());

		return ResponseEntity.ok(tokenResponse);
	}

	private Authentication authenticate(String username, String password) throws Exception {
		try {
			return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>()));
		} catch (DisabledException e) {
			e.printStackTrace();
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			e.printStackTrace();
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}

}
