package com.example.demo.dto.response;

import lombok.Data;

@Data
public class ForgotPasswordResDto {

	private String msg;
	
	private String linkResetPassword;
}
