package com.github.leandropa.springjava11.model;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class JwtResponse implements Serializable {

	private String username;

	private Date expiresAt;

	private final String token;

}
