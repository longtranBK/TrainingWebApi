package com.example.demo.dto.request;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class ResetPasswordReqDto {
	
	@NotBlank(message = "The username is required.")
	private String username;
	
	@NotBlank(message = "The email is required.")
	private String email;
	
	@NotBlank(message = "The resetPasswordToken is required.")
	private String resetPasswordToken;
	
	@NotBlank(message = "The newPassword is required.")
	private String newPassword;
}
