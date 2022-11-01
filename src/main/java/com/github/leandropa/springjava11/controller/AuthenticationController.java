package com.github.leandropa.springjava11.controller;

import com.github.leandropa.springjava11.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

//@RestController
//@RequestMapping("/api/auth")
public class AuthenticationController {

//	@Resource
	private AuthenticationManager authenticationManager;


//	@RequestMapping
	public ResponseEntity<?> auth(@RequestBody User user) throws Exception {

		authenticate(user.getUsername(), user.getPassword());


//		final String token = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(user.getUsername());
	}

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}

}
