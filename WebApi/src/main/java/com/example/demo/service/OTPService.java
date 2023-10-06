package com.example.demo.service;

public interface OTPService {
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	int generateOTP(String key);
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	int getOtp(String key);
	
	/**
	 * 
	 * @param key
	 */
	void clearOTP(String key);
}
