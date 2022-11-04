package com.github.leandropa.springjava11.controller;

import com.github.leandropa.springjava11.entity.User;
import com.github.leandropa.springjava11.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@PostMapping
	public ResponseEntity<?> create(@RequestBody User user) {

		user = userRepository.save(user);

		URI newUserUri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user).toUri();

		return ResponseEntity.created(newUserUri).body(user);
	}
	@GetMapping("/{userId}")
	public ResponseEntity<?> get(@PathVariable ObjectId userId) {

		return userRepository.findById(userId)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}
	@PutMapping("/{userId}")
	public ResponseEntity<?> update(@PathVariable ObjectId userId, @RequestBody User user) {

		return userRepository.findById(userId)
				.map(oldUser -> {
					user.setId(oldUser.getId());
					userRepository.save(user);
					return ResponseEntity.ok(user);
				})
				.orElse(ResponseEntity.notFound().build());	}
	@DeleteMapping("/{userId}")
	public ResponseEntity<?> delete(@PathVariable ObjectId userId) {

		return userRepository.findById(userId)
				.map(user -> {
					userRepository.delete(user);
					return ResponseEntity.ok(user);
				})
				.orElse(ResponseEntity.notFound().build());
	}

	@GetMapping
	public ResponseEntity<?> list() {

		List<User> users = userRepository.findAll();

		return ResponseEntity.ok(users);
	}
}
