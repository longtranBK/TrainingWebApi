package com.example.demo.service;

public interface OTPService {
	
	/**
	 * Gen OTP
	 * 
	 * @param key
	 * @return otp
	 */
	int generateOTP(String key);
	
	/**
	 * Get otp
	 * 
	 * @param key
	 * @return otp
	 */
	int getOtp(String key);
	
	/**
	 * Clear otp
	 * 
	 * @param key
	 */
	void clearOTP(String key);
}
