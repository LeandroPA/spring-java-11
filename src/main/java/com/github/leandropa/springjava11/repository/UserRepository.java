package com.github.leandropa.springjava11.repository;

import com.github.leandropa.springjava11.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, ObjectId> {
}
