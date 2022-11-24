package com.github.leandropa.springjava11.service;

import com.github.leandropa.springjava11.entity.User;
import com.github.leandropa.springjava11.exception.UsernameAlreadyExistsException;
import com.github.leandropa.springjava11.repository.UserRepository;
import lombok.extern.java.Log;
import org.bson.types.ObjectId;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Log
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
	public User insert(User user) throws UsernameAlreadyExistsException {

		if (userRepository.existsByUsername(user.getUsername())) {
			throw new UsernameAlreadyExistsException();
		}

		log.info("Inserting " + user);

		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}

	@Override
	public Optional<User> update(User user) throws UsernameAlreadyExistsException {

		boolean existOtherUserWithUsername = getUserByUsername(user.getUsername())
				.map(oldUser -> !oldUser.getId().equals(user.getId()))
				.orElse(false);

		if (existOtherUserWithUsername) {
			throw new UsernameAlreadyExistsException();
		}

		log.info("Updating " + user);

		return getUserById(user.getId())
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

		log.info("Deleting User: #" + id);

		return getUserById(id)
				.map(user -> {
					userRepository.delete(user);
					return user;
				});
	}
}
