package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.service.OTPService;

@Controller
public class OTPController {
	
	@Autowired
	public OTPService otpService;
	
	@GetMapping("/generateOtp")
	public String generateOTP() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		int otp = otpService.generateOTP(username);
		
		return otp + "";
	}
	
	public @ResponseBody String validateOtp(@RequestParam("otpnum") int otpnum) {
		
		final String SUCCESS = "OTP is valid";
		final String FAIL = "OTP is not valid";
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		
		if(otpnum >=0) {
			int serverOtp = otpService.getOtp(username);
			if(serverOtp > 0) {
				if(otpnum == serverOtp) {
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
