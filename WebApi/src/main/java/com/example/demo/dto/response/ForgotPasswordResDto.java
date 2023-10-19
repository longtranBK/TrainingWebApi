package com.example.demo.dto.response;

import lombok.Data;

@Data
public class ForgotPasswordResDto {

	private String token;
	
	private String msg;
}
