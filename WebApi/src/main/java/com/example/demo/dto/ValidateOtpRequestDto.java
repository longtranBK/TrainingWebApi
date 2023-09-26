package com.example.demo.dto;

import lombok.Data;

@Data
public class ValidateOtpRequestDto {
	
	private String username;
	
	private int otp;
}
