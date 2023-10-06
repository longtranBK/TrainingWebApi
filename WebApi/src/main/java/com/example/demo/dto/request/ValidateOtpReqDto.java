package com.example.demo.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ValidateOtpReqDto {
	
	@Email
	@NotBlank(message = "The username is required.")
	private String username;
	
	@NotNull
	private int otp;
}
