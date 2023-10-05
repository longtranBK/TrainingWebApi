package com.example.demo.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class ForgotPasswordReqDto {
	
	@Email
	@NotBlank(message = "The username is required.")
	private String username;

}
