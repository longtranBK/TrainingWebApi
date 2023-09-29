package com.example.demo.dto;

import lombok.Data;

@Data
public class ForgotPasswordResDto {

	private String msg;
	
	private String linkResetPassword;
}
