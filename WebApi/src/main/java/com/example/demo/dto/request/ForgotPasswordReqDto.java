package com.example.demo.dto.request;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class ForgotPasswordReqDto {
	
	@NotBlank(message = "The username is required.")
	private String username;
	
	@NotBlank(message = "The email is required.")
	private String email;
}
