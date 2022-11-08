package com.github.leandropa.springjava11.service;

import com.github.leandropa.springjava11.entity.User;
import com.github.leandropa.springjava11.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userRepository = userRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
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
	public Optional<User> getUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public User insert(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}

	@Override
	public Optional<User> update(ObjectId id, User user) {
		return getUserById(id)
				.map(oldUser -> mapUpdate(oldUser, user))
				.map(userRepository::save);
	}

	private User mapUpdate(User oldUser, User newUser) {
		newUser.setId(oldUser.getId());
		if (!Objects.equals(oldUser.getPassword(), newUser.getPassword())) {
			newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
		}
		return newUser;
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
