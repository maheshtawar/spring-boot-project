/**
 * 
 */
package com.example.spring_boot_project.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.example.spring_boot_project.model.Position;
import com.example.spring_boot_project.repository.PositionRepository;

/**
 * @author MaheshT
 *
 */
@Service
public class PositionServiceImpl implements PositionService {

	@Autowired
	private PositionRepository positionRepository;

	@Autowired
	@Qualifier("reader")
	JdbcTemplate reader;

	@Autowired
	@Qualifier("writer")
	JdbcTemplate writer;

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public List<Position> getAllPositions() throws Exception {
		try {
			return positionRepository.getAllPositions(reader);
		} catch (Exception e) {
			logger.error("Unexpected error fetching employees: " + e.getMessage());
			throw new Exception("Unexpected error occurred while fetching employees.");
		}
	}

}
