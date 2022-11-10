package com.github.leandropa.springjava11.controller;

import com.github.leandropa.springjava11.validation.UserAuthentication;
import com.github.leandropa.springjava11.entity.User;
import com.github.leandropa.springjava11.model.JwtResponse;
import com.github.leandropa.springjava11.util.JwtTokenUtil;
import com.github.leandropa.springjava11.util.SecurityConstants;
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

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

	@Resource
	private JwtTokenUtil jwtTokenUtil;
	@Resource
	private AuthenticationManager authenticationManager;

	@PostMapping
	public ResponseEntity<?> auth(@Validated(UserAuthentication.class) @RequestBody User user) throws Exception {

		Authentication auth = authenticate(user.getUsername(), user.getPassword());

		final JwtResponse token = jwtTokenUtil.generateAccessToken(user.getUsername());

		return ResponseEntity.ok(token);
	}
	@GetMapping("/token")
	public ResponseEntity<?> tokenInfo(@RequestHeader(SecurityConstants.HEADER_STRING) String authHeader) {

		String token = authHeader.replaceFirst(SecurityConstants.TOKEN_PREFIX, "");

		return ResponseEntity.ok(jwtTokenUtil.getAccessToken(token));
	}

	private Authentication authenticate(String username, String password) throws Exception {
		try {
			return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>()));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}

}
