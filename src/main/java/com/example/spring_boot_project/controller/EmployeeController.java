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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.employeedirectorysystem.model.Employee;
import com.example.employeedirectorysystem.service.EmployeeService;

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

	@GetMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_HR')")
	public Employee getEmployeeById(@PathVariable int id) {
		try {
			return employeeService.getEmployeeById(id);
		} catch (Exception e) {
			logger.error("Error fetching employee by id: " + e.getMessage(), e);
			throw new RuntimeException("Unexpected error occurred while fetching the employee.");
		}
	}

	@PostMapping("/add")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public long addEmployee(@RequestBody Employee employee) {
		try {
			return employeeService.addEmployee(employee);
		} catch (Exception e) {
			logger.error("Error adding employee: " + e.getMessage(), e);
			throw new RuntimeException("Unexpected error occurred while adding an employee.");
		}
	}

	@PutMapping("/update")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public void updateEmployee(@RequestBody Employee employee) {
		try {
			employeeService.updateEmployee(employee);
		} catch (Exception e) {
			logger.error("Error updating employee: " + e.getMessage(), e);
			throw new RuntimeException("Unexpected error occurred while updating the employee.");
		}
	}

	@DeleteMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public void deleteEmployee(@PathVariable int id) {
		try {
			employeeService.deleteEmployee(id);
		} catch (Exception e) {
			logger.error("Error deleting employee: " + e.getMessage(), e);
			throw new RuntimeException("Unexpected error occurred while deleting the employee.");
		}
	}
}
