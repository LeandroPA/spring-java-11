package com.github.leandropa.springjava11.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.leandropa.springjava11.validation.UserAuthentication;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;

@Document("users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class User {

	@Id
	@JsonSerialize(using = ToStringSerializer.class)
	private ObjectId id;

	@Indexed(unique = true)
	@NotBlank(groups = UserAuthentication.class)
	private String username;

	@NotBlank(groups = UserAuthentication.class)
	private String password;

	@Override
	public String toString() {
		return "User(" +
				"id=" + id + ", " +
				"username=" + username + ", " +
				"password=" + (password != null && !password.isBlank() ? "********" : password) + ")";
	}
}
