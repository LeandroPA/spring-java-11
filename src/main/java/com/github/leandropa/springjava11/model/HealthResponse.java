package com.github.leandropa.springjava11.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class HealthResponse {

	private String version;

	@JsonProperty("commit_id")
	private String commitId;

}
