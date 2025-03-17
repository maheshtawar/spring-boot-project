/**
 * 
 */
package com.example.spring_boot_project.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.example.spring_boot_project.model.Employee;

/**
 * @author MaheshT
 *
 */
@Repository
public class EmployeeRepository {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	public List<Employee> getAllEmployees(JdbcTemplate reader) throws Exception {
		try {
			return reader.query("SELECT  `employee_id` 'employeeId', `full_name` 'fullName', `department`, `contact_number` 'contactNumber', `email`, `date_of_joining` 'dateOfJoining', `position_id` 'positionId' FROM `employeedirectory`.`employees` ;", new BeanPropertyRowMapper<>(Employee.class));
		} catch (DataAccessException e) {
			logger.error("Database error while fetching employees: " + e.getMessage());
			throw new Exception("Database error: Unable to retrieve employees.");
		}
	}

	public long addEmployee(Employee employee, JdbcTemplate writer) throws Exception {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		try {
			StringBuilder query = new StringBuilder();
			query.append(
					"INSERT INTO employees (full_name, department, contact_number, email, date_of_joining, created_by, updated_by, position_id) ");
			query.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?);");

			writer.update(con -> {
				PreparedStatement ps = con.prepareStatement(query.toString(), Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, employee.getFullName());
				ps.setString(2, employee.getDepartment());
				ps.setString(3, employee.getContactNumber());
				ps.setString(4, employee.getEmail());
				ps.setDate(5, employee.getDateOfJoining());
				ps.setInt(6, employee.getCreatedBy());
				ps.setInt(7, employee.getUpdatedBy());
				ps.setInt(8, employee.getPositionId());
				return ps;
			}, keyHolder);

			return keyHolder.getKey().longValue();
		} catch (DataAccessException e) {
			logger.error("Database error while adding employee: " + e.getMessage());
			throw new Exception("Database error: Unable to add employee.");
		}
	}

	public int updateEmployee(Employee employee, JdbcTemplate writer) throws Exception {
		try {
			StringBuilder query = new StringBuilder();
			query.append("UPDATE employees SET full_name=?, department=?, contact_number=?, email=?, ");
			query.append("date_of_joining=?, updated_by=?, position_id=? WHERE employee_id=?");

			return writer.update(query.toString(), employee.getFullName(), employee.getDepartment(),
					employee.getContactNumber(), employee.getEmail(), employee.getDateOfJoining(),
					employee.getUpdatedBy(), employee.getPositionId(), employee.getEmployeeId());
		} catch (DataAccessException e) {
			logger.error("Database error while updating employee: " + e.getMessage());
			throw new Exception("Database error: Unable to update employee.");
		}
	}

	public int deleteEmployee(int id, JdbcTemplate writer) throws Exception {
		try {
			return writer.update("DELETE FROM employees WHERE employee_id=?", id);
		} catch (DataAccessException e) {
			logger.error("Database error while deleting employee: " + e.getMessage());
			throw new Exception("Database error: Unable to delete employee.");
		}
	}
}