package com.github.leandropa.springjava11.controller;

import com.github.leandropa.springjava11.model.HealthResponse;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api")
public class HealthController {

	@Resource
	private BuildProperties buildProperties;

	@GetMapping("/health")
	public ResponseEntity<HealthResponse> health() {
		return ResponseEntity.ok(new HealthResponse(
				buildProperties.getVersion(), buildProperties.get("build.commit_id")));
	}
}
