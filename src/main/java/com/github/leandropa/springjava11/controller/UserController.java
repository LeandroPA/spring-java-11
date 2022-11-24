package com.github.leandropa.springjava11.controller;

import com.github.leandropa.springjava11.entity.User;
import com.github.leandropa.springjava11.exception.UsernameAlreadyExistsException;
import com.github.leandropa.springjava11.repository.UserRepository;
import com.github.leandropa.springjava11.service.UserService;
import lombok.extern.java.Log;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Log
@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping
	public ResponseEntity<?> create(@RequestBody User user) throws UsernameAlreadyExistsException {

		user = userService.insert(user);

		URI newUserUri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();

		return ResponseEntity.created(newUserUri).body(user);
	}

	@GetMapping("/{userId}")
	public ResponseEntity<?> get(@PathVariable ObjectId userId) {

		return userService.getUserById(userId)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PutMapping("/{userId}")
	public ResponseEntity<?> update(@PathVariable ObjectId userId, @RequestBody User user) throws UsernameAlreadyExistsException {

		user.setId(userId);

		return userService.update(user)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{userId}")
	public ResponseEntity<?> delete(@PathVariable ObjectId userId) {

		return userService.delete(userId)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@GetMapping
	public ResponseEntity<?> list() {

		List<User> users = userService.getAll();

		return ResponseEntity.ok(users);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationExceptions(
			MethodArgumentNotValidException ex) {

		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});

		log.info("handleValidationExceptions :: " + errors);

		return errors;
	}
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(UsernameAlreadyExistsException.class)
	public Map<String, String> handleUsernameAlreadyExistsException(
			UsernameAlreadyExistsException ex) {

		Map<String, String> errors = new HashMap<>();

		errors.put("username", ex.getMessage());

		log.info("handleUsernameAlreadyExistsException :: " + errors);


		return errors;
	}

}
