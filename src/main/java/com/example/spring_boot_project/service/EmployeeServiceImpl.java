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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.employeedirectorysystem.model.Employee;
import com.example.employeedirectorysystem.repository.EmployeeRepository;

/**
 * @author MaheshT
 *
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {
	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	@Qualifier("reader")
	JdbcTemplate reader;

	@Autowired
	@Qualifier("writer")
	JdbcTemplate writer;

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
		public List<Employee> getAllEmployees() throws Exception {
		try {
			return employeeRepository.getAllEmployees(reader);
		} catch (Exception e) {
			logger.error("Unexpected error fetching employees: " + e.getMessage());
			throw new Exception("Unexpected error occurred while fetching employees.");
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public long addEmployee(Employee employee) throws Exception {
		try {
			long employeeId = employeeRepository.addEmployee(employee, writer);
			if (employeeId == 0) {
				throw new Exception("Failed to add employee.");
			}
			return employeeId;
		} catch (Exception e) {
			logger.error("Unexpected error adding employee: " + e.getMessage());
			throw new Exception("Unexpected error occurred while adding an employee.");
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateEmployee(Employee employee) throws Exception {
		try {
			int rowsAffected = employeeRepository.updateEmployee(employee, writer);
			if (rowsAffected == 0) {
				throw new Exception("Employee not found or no updates made.");
			}
		} catch (Exception e) {
			logger.error("Unexpected error updating employee: " + e.getMessage());
			throw new Exception("Unexpected error occurred while updating the employee.");
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void deleteEmployee(int id) throws Exception {
		try {
			int rowsAffected = employeeRepository.deleteEmployee(id, writer);
			if (rowsAffected == 0) {
				throw new Exception("Employee not found or already deleted.");
			}
		} catch (Exception e) {
			logger.error("Unexpected error deleting employee: " + e.getMessage());
			throw new Exception("Unexpected error occurred while deleting the employee.");
		}
	}

	@Override
		public Employee getEmployeeById(int id) throws Exception {
		try {
			return employeeRepository.getEmployeeById(id, reader);
		} catch (Exception e) {
			logger.error("Unexpected error fetching employee by id: " + e.getMessage());
			throw new Exception("Unexpected error occurred while fetching the employee.");
		}
	}
}
