package com.example.demo.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class ValidateOtpReqDto {
	
	@Size(max = 100)
	@Email
	@NotBlank(message = "The username is required.")
	private String username;
	
	@Min(100000)
	@Max(999999)
	@NotNull(message = "The sex is required.")
	private int otp;
}
