package com.example.demo.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ValidateOtpRequestDto {
	
	@NotBlank(message = "The username is required.")
	private String username;
	
	@NotNull
	private int otp;
}
