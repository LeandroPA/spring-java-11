package com.github.leandropa.springjava11.repository;

import com.github.leandropa.springjava11.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {

	Optional<User> findByUsername(String username);

	boolean existsByUsername(String username);
}
