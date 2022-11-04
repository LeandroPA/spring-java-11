package com.github.leandropa.springjava11.service;

import com.github.leandropa.springjava11.entity.User;
import com.github.leandropa.springjava11.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

	UserRepository userRepository;

	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public List<User> getAll() {
		return userRepository.findAll();
	}

	@Override
	public Optional<User> getUserById(ObjectId id) {
		return userRepository.findById(id);
	}

	@Override
	public User insert(User user) {
		return userRepository.save(user);
	}

	@Override
	public Optional<User> update(ObjectId id, User user) {
		return getUserById(id)
				.map(oldUser -> {
					user.setId(oldUser.getId());
					return user;
				})
				.map(newUser -> userRepository.save(newUser));
	}

	@Override
	public Optional<User> delete(ObjectId id) {
		return getUserById(id)
				.map(user -> {
					userRepository.delete(user);
					return user;
				});
	}
}
