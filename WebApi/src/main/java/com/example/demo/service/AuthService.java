package com.example.demo.service;

public interface AuthService {

	/**
	 * Check active of user
	 * 
	 * @param username
	 * @return true if active
	 */
	boolean isActive(String username);
}
