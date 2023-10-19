package com.example.demo.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class ValidateOtpReqDto {
	
	@Size(max = 100)
	@NotBlank(message = "The username is required.")
	private String username;
	
	@NotNull(message = "Otp is required.")
	private int otp;
}
