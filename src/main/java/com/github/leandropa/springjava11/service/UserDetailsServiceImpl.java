package com.github.leandropa.springjava11.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Resource
	private UserService userService;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userService.getUserByUsername(username)
				.map(UserDetailsImpl::of)
				.orElseThrow(() -> new UsernameNotFoundException("Username not found"));
	}
}