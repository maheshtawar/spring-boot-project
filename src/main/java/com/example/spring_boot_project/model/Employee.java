/**
 * 
 */
package com.example.spring_boot_project.model;

import java.sql.Date;
import java.sql.Timestamp;

import lombok.Data;

/**
 * @author MaheshT
 *
 */
@Data
public class Employee {
	private int employeeId;
	private String fullName;
	private String department;
	private String contactNumber;
	private String email;
	private Date dateOfJoining;
	private Timestamp createdOn;
	private int createdBy;
	private Timestamp updatedOn;
	private int updatedBy;
	private int positionId;
	private String position;
}
