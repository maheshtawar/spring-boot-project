package com.example.spring_boot_project.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.spring_boot_project.model.User;
import com.example.spring_boot_project.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	@Qualifier("reader")
	private JdbcTemplate reader;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email, reader);
		if (user == null) {
			throw new UsernameNotFoundException("User not found with email: " + email);
		}

		// Prefix role with "ROLE_" to ensure compatibility with Spring Security
		String roleWithPrefix = "ROLE_" + user.getRole().toUpperCase(); // e.g., "ROLE_ADMIN"
		GrantedAuthority authority = new SimpleGrantedAuthority(roleWithPrefix);

		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
				Collections.singletonList(authority));
	}

}