package com.github.leandropa.springjava11.exception;

public class UsernameAlreadyExistsException extends Exception {
	public UsernameAlreadyExistsException() {
		this("Username already exists.");
	}

	public UsernameAlreadyExistsException(String message) {
		super(message);
	}
}
