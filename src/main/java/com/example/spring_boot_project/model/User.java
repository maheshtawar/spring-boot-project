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

	// No-args constructor
	public User() {
	}

	// All-args constructor
	public User(int id, String name, String email, String password, String role, Timestamp createdOn) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.role = role;
		this.createdOn = createdOn;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Timestamp getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}

}
