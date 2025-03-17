/**
 * 
 */
package com.example.spring_boot_project.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring_boot_project.model.Employee;
import com.example.spring_boot_project.service.EmployeeService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author MaheshT
 *
 */
@RestController
@RequestMapping("/employees")
@SecurityRequirement(name = "BearerAuth")
public class EmployeeController {

	private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

	private final EmployeeService employeeService;

	public EmployeeController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	@GetMapping("/all")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_HR')")
	public List<Employee> getAllEmployees() {
		try {
			return employeeService.getAllEmployees();
		} catch (Exception e) {
			logger.error("Error fetching employees: " + e.getMessage(), e);
			throw new RuntimeException("Unexpected error occurred while fetching employees.");
		}
	}
}