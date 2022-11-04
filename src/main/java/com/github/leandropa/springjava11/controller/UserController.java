package com.github.leandropa.springjava11.controller;

import com.github.leandropa.springjava11.entity.User;
import com.github.leandropa.springjava11.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@PostMapping
	public ResponseEntity<?> create(@RequestBody User user) {

		user = userRepository.save(user);

		return ResponseEntity.ok(user);
	}}
