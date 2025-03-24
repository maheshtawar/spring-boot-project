/**
 * 
 */
package com.example.spring_boot_project.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring_boot_project.model.Position;
import com.example.spring_boot_project.service.PositionService;

/**
 * @author MaheshT
 *
 */
@RestController
@RequestMapping
public class PositionController {

	private static final Logger logger = LoggerFactory.getLogger(PositionController.class);

	@Autowired
	private PositionService positionService;

	@GetMapping("/public/getPositions")
	public List<Position> getAllPositions() {
		try {
			return positionService.getAllPositions();
		} catch (Exception e) {
			logger.error("Error fetching Positions: " + e.getMessage());
			throw new RuntimeException("Unexpected error occurred while fetching Positions.");
		}
	}
}
