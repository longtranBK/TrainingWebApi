package com.example.demo.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class ForgotPasswordRequestDto {
	
	@NotBlank(message = "The username is required.")
	private String username;
	
	@NotBlank(message = "The email is required.")
	private String email;
}
