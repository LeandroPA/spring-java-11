package com.github.leandropa.springjava11.service;

import com.github.leandropa.springjava11.entity.User;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Optional;

public interface UserService {

	List<User> getAll();

	Optional<User> getUserById(ObjectId id);

	Optional<User> getUserByUsername(String username);

	User insert(User user);

	Optional<User> update(ObjectId id, User user);

	Optional<User> delete(ObjectId id);

}
