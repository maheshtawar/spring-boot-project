/**
 * 
 */
package com.example.spring_boot_project.service;

import java.util.List;

import com.example.spring_boot_project.model.Position;

/**
 * @author MaheshT
 *
 */
public interface PositionService {
	List<Position> getAllPositions() throws Exception;
}
