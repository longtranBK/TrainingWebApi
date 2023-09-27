package com.example.demo.dto;

import lombok.Data;

@Data
public class ForgotPasswordResponseDto {

	private String msg;
	
	private String linkResetPassword;
}
