package com.example.spring_boot_project.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring_boot_project.model.User;
import com.example.spring_boot_project.repository.UserRepository;
import com.example.spring_boot_project.utility.JwtTokenUtility;

/**
 * Authentication Controller (JWT-based) Handles User Registration and Login
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private JwtTokenUtility jwtTokenUtility;

	@Autowired
	@Qualifier("reader")
	private JdbcTemplate reader;

	@Autowired
	@Qualifier("writer")
	private JdbcTemplate writer;

	/**
	 * User Registration API
	 */
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody User user) {
		try {
			long userId = userRepository.registerUser(new User(user.getId(), user.getName(), user.getEmail(),
					passwordEncoder.encode(user.getPassword()), user.getRole(), null), writer);

			return ResponseEntity.ok(Map.of("message", "User registered successfully", "userId", userId));
		} catch (DataAccessException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "Database error: Unable to register user"));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "Unexpected error occurred while registering user"));
		}
	}

	/**
	 * User Login API - Generates JWT Token
	 */
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
		try {
			User user = userRepository.findByEmail(credentials.get("email"), reader);

			if (user != null && passwordEncoder.matches(credentials.get("password"), user.getPassword())) {
				// Generate JWT token
				String token = jwtTokenUtility.generateToken(user.getEmail(),user.getRole());

				return ResponseEntity.ok(Map.of("message", "Login successful", "token", token, "role", user.getRole()));
			}

			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid email or password"));
		} catch (EmptyResultDataAccessException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid email or password"));
		} catch (DataAccessException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "Database error: Unable to process login"));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "Unexpected error occurred while processing login"));
		}
	}
}
