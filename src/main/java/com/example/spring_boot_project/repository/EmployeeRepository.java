/**
 * 
 */
package com.example.spring_boot_project.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.example.spring_boot_project.model.Employee;
import com.example.spring_boot_project.utility.JwtTokenUtility;

/**
 * @author MaheshT
 *
 */
@Repository
public class EmployeeRepository {

	@Autowired
	JwtTokenUtility jwtTokenUtility;

	Logger logger = LoggerFactory.getLogger(this.getClass());

	public List<Employee> getAllEmployees(JdbcTemplate reader, String search) throws Exception {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append(
					"SELECT `employee_id` 'employeeId', `full_name` 'fullName', `department`, `contact_number` 'contactNumber', `email`, `date_of_joining` 'dateOfJoining', `position_id` 'positionId' ");
			sql.append("FROM `employeedirectory`.`employees`WHERE 1=1");

			List<Object> params = new ArrayList<>();

			if (search != null && !search.isEmpty()) {
				sql.append(" AND (full_name LIKE ?");
				sql.append(" OR department LIKE ?)");
				params.add("%" + search + "%");
				params.add("%" + search + "%");
			}
			sql.append(";");

			return reader.query(sql.toString(), new BeanPropertyRowMapper<>(Employee.class), params.toArray());
		} catch (DataAccessException e) {
			logger.error("Database error while fetching employees: " + e.getMessage());
			throw new Exception("Database error: Unable to retrieve employees.");
		}
	}

	public long addEmployee(Employee employee, JdbcTemplate writer) throws Exception {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		try {
			StringBuilder sql = new StringBuilder();
			sql.append(
					"INSERT INTO employees (full_name, department, contact_number, email, date_of_joining, created_by,  position_id) ");
			sql.append("VALUES (?, ?, ?, ?, ?, ?, ?);");

			writer.update(con -> {
				PreparedStatement ps = con.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, employee.getFullName());
				ps.setString(2, employee.getDepartment());
				ps.setString(3, employee.getContactNumber());
				ps.setString(4, employee.getEmail());
				ps.setDate(5, employee.getDateOfJoining());
				ps.setString(6, jwtTokenUtility.getCurrentUsername());
				ps.setInt(7, employee.getPositionId());
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
			sql.append(
					"UPDATE employees SET full_name=?, department=?, contact_number=?, email=?, date_of_joining=?, updated_by=?, position_id=? ");
			sql.append("WHERE employee_id=?");

			return writer.update(sql.toString(), employee.getFullName(), employee.getDepartment(),
					employee.getContactNumber(), employee.getEmail(), employee.getDateOfJoining(),
					jwtTokenUtility.getCurrentUsername(), employee.getPositionId(), employee.getEmployeeId());
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
			sql.append(" SELECT ");
			sql.append(" e.`employee_id` 'employeeId', ");
			sql.append(" e.`full_name` 'fullName', ");
			sql.append(" e.`department`, ");
			sql.append(" e.`contact_number` 'contactNumber', ");
			sql.append(" e.`email`, ");
			sql.append(" e.`date_of_joining` 'dateOfJoining', ");
			sql.append(" e.`position_id` 'positionId' ");
			sql.append(" FROM ");
			sql.append(" `employeedirectory`.`employees` e ");
			sql.append(" WHERE `employee_id` = ?; ");

			return reader.queryForObject(sql.toString(), new BeanPropertyRowMapper<>(Employee.class), id);
		} catch (DataAccessException e) {
			logger.error("Database error while fetching employee by id: " + e.getMessage());
			throw new Exception("Database error: Unable to retrieve employee.");
		}
	}
}