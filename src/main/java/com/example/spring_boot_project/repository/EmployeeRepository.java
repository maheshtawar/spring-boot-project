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

import com.example.employeedirectorysystem.model.Employee;

/**
 * @author MaheshT
 *
 */
@Repository
public class EmployeeRepository {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	public List<Employee> getAllEmployees(JdbcTemplate reader) throws Exception {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT `employee_id` 'employeeId', `full_name` 'fullName', `department`, `contact_number` 'contactNumber', `email`, `date_of_joining` 'dateOfJoining', `position_id` 'positionId' ");
			sql.append("FROM `employeedirectory`.`employees`;");
			return reader.query(sql.toString(), new BeanPropertyRowMapper<>(Employee.class));
		} catch (DataAccessException e) {
			logger.error("Database error while fetching employees: " + e.getMessage());
			throw new Exception("Database error: Unable to retrieve employees.");
		}
	}

	public long addEmployee(Employee employee, JdbcTemplate writer) throws Exception {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO employees (full_name, department, contact_number, email, date_of_joining, created_by, updated_by, position_id) ");
			sql.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?);");

			writer.update(con -> {
				PreparedStatement ps = con.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
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
			StringBuilder sql = new StringBuilder();
			sql.append("UPDATE employees SET full_name=?, department=?, contact_number=?, email=?, date_of_joining=?, updated_by=?, position_id=? ");
			sql.append("WHERE employee_id=?");

			return writer.update(sql.toString(), employee.getFullName(), employee.getDepartment(), employee.getContactNumber(), employee.getEmail(), employee.getDateOfJoining(), employee.getUpdatedBy(), employee.getPositionId(), employee.getEmployeeId());
		} catch (DataAccessException e) {
			logger.error("Database error while updating employee: " + e.getMessage());
			throw new Exception("Database error: Unable to update employee.");
		}
	}

	public int deleteEmployee(int id, JdbcTemplate writer) throws Exception {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("DELETE FROM employees WHERE employee_id=?");

			return writer.update(sql.toString(), id);
		} catch (DataAccessException e) {
			logger.error("Database error while deleting employee: " + e.getMessage());
			throw new Exception("Database error: Unable to delete employee.");
		}
	}

	public Employee getEmployeeById(int id, JdbcTemplate reader) throws Exception {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT `employee_id` 'employeeId', `full_name` 'fullName', `department`, `contact_number` 'contactNumber', `email`, `date_of_joining` 'dateOfJoining', `position_id` 'positionId' ");
			sql.append("FROM `employeedirectory`.`employees` ");
			sql.append("WHERE `employee_id` = ?");

			return reader.queryForObject(sql.toString(), new BeanPropertyRowMapper<>(Employee.class), id);
		} catch (DataAccessException e) {
			logger.error("Database error while fetching employee by id: " + e.getMessage());
			throw new Exception("Database error: Unable to retrieve employee.");
		}
	}
}
