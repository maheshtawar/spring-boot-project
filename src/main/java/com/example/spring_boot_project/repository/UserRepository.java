/**
 * 
 */
package com.example.spring_boot_project.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.example.employeedirectorysystem.model.User;

/**
 * @author MaheshT
 *
 */
@Repository
public class UserRepository {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());

	public long registerUser(User user, JdbcTemplate writer) throws Exception {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO users (name, email, password, role, created_on) ");
			sql.append("VALUES (?, ?, ?, ?, NOW());");

			writer.update(con -> {
				PreparedStatement ps = con.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, user.getName());
				ps.setString(2, user.getEmail());
				ps.setString(3, user.getPassword());
				ps.setString(4, user.getRole());
				return ps;
			}, keyHolder);

			return keyHolder.getKey().longValue();
		} catch (DataAccessException e) {
			logger.error("Database error while registering user: " + e.getMessage());
			throw new Exception("Database error: Unable to register user.");
		}
	}

	public User findByEmail(String email, JdbcTemplate reader) {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT id, name, email, password, role, created_on AS createdOn ");
			sql.append("FROM users ");
			sql.append("WHERE email = ?;");

			return reader.queryForObject(sql.toString(), new BeanPropertyRowMapper<>(User.class), email);
		} catch (EmptyResultDataAccessException e) {
			logger.error("User not found with email: " + email);
			return null; // Return null if user is not found (Avoid exceptions breaking flow)
		}
	}

}
