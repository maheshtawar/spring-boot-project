/**
 * 
 */
package com.example.spring_boot_project.service;

import java.util.List;

import com.example.employeedirectorysystem.model.Employee;

/**
 * @author MaheshT
 *
 */
public interface EmployeeService {
	List<Employee> getAllEmployees() throws Exception;

	long addEmployee(Employee employee) throws Exception;

	void updateEmployee(Employee employee) throws Exception;

	void deleteEmployee(int id) throws Exception;

	Employee getEmployeeById(int id) throws Exception;
}
