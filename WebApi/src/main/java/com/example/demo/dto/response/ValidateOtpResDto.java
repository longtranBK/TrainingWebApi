package com.example.demo.dto.response;

import lombok.Data;

@Data
public class ValidateOtpResDto {

	private String userId;
	
	private String token;
	
	private String msg;
}
