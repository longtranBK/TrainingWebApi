package com.example.demo.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class ForgotPasswordReqDto {
	
	@Size(max = 100)
	@Email
	@NotBlank(message = "The username is required.")
	private String username;

}
