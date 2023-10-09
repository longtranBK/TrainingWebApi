package com.example.demo.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class ResetPasswordReqDto {
	
	@Size(max = 20)
	@NotBlank(message = "The newPassword is required.")
	private String newPassword;
}
