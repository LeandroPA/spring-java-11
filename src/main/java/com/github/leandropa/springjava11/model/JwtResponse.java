package com.github.leandropa.springjava11.model;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class JwtResponse implements Serializable {

	private final String token;

}
