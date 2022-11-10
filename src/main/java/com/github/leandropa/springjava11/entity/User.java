package com.github.leandropa.springjava11.entity;

import com.github.leandropa.springjava11.validation.UserAuthentication;
import lombok.*;
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
@ToString
public class User {

	@Id
	private String id;

	@Indexed(unique = true)
	@NotBlank(groups = UserAuthentication.class)
	private String username;

	@NotBlank(groups = UserAuthentication.class)
	private String password;

}
