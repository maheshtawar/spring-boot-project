/**
 * 
 */
package com.example.spring_boot_project.repository;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.spring_boot_project.model.Position;

/**
 * @author MaheshT
 *
 */
@Repository
public class PositionRepository {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	public List<Position> getAllPositions(JdbcTemplate reader) throws Exception {
		try {
			String query = "SELECT position_id, position FROM positions";
			return reader.query(query, new BeanPropertyRowMapper<>(Position.class));
		} catch (DataAccessException e) {
			logger.error("Database error while fetching positions: " + e.getMessage());
			throw new Exception("Database error: Unable to retrieve positions.");
		} catch (Exception e) {
			logger.error("Unexpected error fetching positions: " + e.getMessage());
			throw new Exception("Unexpected error occurred while fetching positions.");
		}
	}

}
