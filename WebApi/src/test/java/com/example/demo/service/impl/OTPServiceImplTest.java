package com.example.demo.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OTPServiceImplTest {
	@Autowired
	private OTPServiceImpl otpServiceImpl;

	@Test
	void generateOTP_withInput_returnOtp() {
		int otp = otpServiceImpl.generateOTP("key");
		assertNotEquals(0, otp);
	}

	@Test
	void getOtp_withInput_returnOtp() {
		int otp = otpServiceImpl.getOtp("key");
		assertNotEquals(0, otp);
	}

	@Test
	void getOtp_withKeyIsNull_return0() {
		int otp = otpServiceImpl.getOtp(null);
		assertEquals(0, otp);
	}

	@Test
	void clearOTP_withInput_excuteSuscess() {
		otpServiceImpl.clearOTP("key");
	}
}
