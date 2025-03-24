/**
 * 
 */
package com.example.spring_boot_project.model;

import java.sql.Timestamp;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author MaheshT
 *
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
	private int id;
	private String name;
	@NotNull
	@NotEmpty
	private String email;
	private String password;
	private String role;
	private Timestamp createdOn;
}
