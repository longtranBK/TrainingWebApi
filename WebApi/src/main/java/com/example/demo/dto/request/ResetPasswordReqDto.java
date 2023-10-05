package com.example.demo.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class ResetPasswordReqDto {
	
	@Email
	@NotBlank(message = "The username is required.")
	private String username;
	
	@NotBlank(message = "The resetPasswordToken is required.")
	private String resetPasswordToken;
	
	@NotBlank(message = "The newPassword is required.")
	private String newPassword;
}
