/**
 * 
 */
package com.example.spring_boot_project.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.example.spring_boot_project.model.User;

/**
 * @author MaheshT
 *
 */
@Repository
public class UserRepository {

	public long registerUser(User user, JdbcTemplate writer) throws Exception {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		StringBuilder query = new StringBuilder();
		query.append("INSERT INTO users (name, email, password, role, created_on) ");
		query.append("VALUES (?, ?, ?, ?, NOW());");

		writer.update(con -> {
			PreparedStatement ps = con.prepareStatement(query.toString(), Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, user.getName());
			ps.setString(2, user.getEmail());
			ps.setString(3, user.getPassword());
			ps.setString(4, user.getRole());
			return ps;
		}, keyHolder);

		return keyHolder.getKey().longValue();
	}

	public User findByEmail(String email, JdbcTemplate reader) {
		try {
			StringBuilder query = new StringBuilder();
			query.append("SELECT ");
			query.append("   id, ");
			query.append("   name, ");
			query.append("   email, ");
			query.append("   password, ");
			query.append("   role, ");
			query.append("   created_on AS createdOn ");
			query.append("FROM users ");
			query.append("WHERE email = ?;");

			return reader.queryForObject(query.toString(), new BeanPropertyRowMapper<>(User.class), email);
		} catch (EmptyResultDataAccessException e) {
			return null; // Return null if user is not found (Avoid exceptions breaking flow)
		}
	}

}
