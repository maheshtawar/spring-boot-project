/**
 * 
 */
package com.example.spring_boot_project.service;

import com.example.spring_boot_project.model.User;

/**
 * @author MaheshT
 *
 */
public interface UserService {
	public long registerUser(User user) throws Exception;

	public User findByEmail(String email) throws Exception;
}
