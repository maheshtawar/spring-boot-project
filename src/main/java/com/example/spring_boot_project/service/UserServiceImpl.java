/**
 * 
 */
package com.example.spring_boot_project.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.example.spring_boot_project.model.User;
import com.example.spring_boot_project.repository.UserRepository;

/**
 * @author MaheshT
 *
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	@Qualifier("reader")
	JdbcTemplate reader;

	@Autowired
	@Qualifier("writer")
	JdbcTemplate writer;

	@Autowired
	private UserRepository userRepository;

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public long registerUser(User user) throws Exception {
		long userId = 0;
		try {
			// Save user details and get generated ID
			userId = userRepository.registerUser(user, writer);
		} catch (DataAccessException e) {
			logger.error("Database error while registering user: " + e.getMessage());
			throw new Exception("Failed to register user due to a database error.");
		} catch (Exception e) {
			logger.error("Unexpected error while registering user: " + e.getMessage());
			throw new Exception("An unexpected error occurred while registering the user.");
		}
		return userId;
	}

	@Override
	public User findByEmail(String email) throws Exception {
		try {
			return userRepository.findByEmail(email, reader);
		} catch (EmptyResultDataAccessException e) {
			logger.warn("User not found with email: " + email);
			return null; // Return null instead of throwing an exception
		} catch (DataAccessException e) {
			logger.error("Database error while retrieving user: " + e.getMessage());
			throw new Exception("Failed to retrieve user due to a database error.");
		} catch (Exception e) {
			logger.error("Unexpected error while retrieving user: " + e.getMessage());
			throw new Exception("An unexpected error occurred while retrieving the user.");
		}
	}

}
