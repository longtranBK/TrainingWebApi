package com.example.demo.dto.request;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class ResetPasswordReqDto {
	
	@NotBlank(message = "The newPassword is required.")
	private String newPassword;
}
