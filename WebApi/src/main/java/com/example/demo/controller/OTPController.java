package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.dto.OtpDto;
import com.example.demo.service.OTPService;

@Controller
public class OTPController {
	
	@Autowired
	public OTPService otpService;
	
	public String generateOTP() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		int otp = otpService.generateOTP(username);
		
		return otp + "";
	}
	
	@PostMapping(value = { "/validateOtp" })
	public @ResponseBody String validateOtp(@RequestBody OtpDto otpDto) {
		
		final String SUCCESS = "OTP is valid";
		final String FAIL = "OTP is not valid";
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		int requestOtp = otpDto.getOtp();
		
		if(requestOtp >=0) {
			int serverOtp = otpService.getOtp(username);
			if(serverOtp > 0) {
				if(requestOtp == serverOtp) {
					otpService.clearOTP(username);
				}
				return SUCCESS;
			} else {
				return FAIL;
			}
		} else {
			return FAIL;
		}
	}
	
	
}
