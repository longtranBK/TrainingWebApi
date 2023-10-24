package com.example.demo.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ValidateOtpReqDto {
	
	@Schema(type = "string", example = "longth")
	@Size(max = 100)
	@NotBlank(message = "The username is required.")
	private String username;
	
	@Schema(type = "int", example = "111111")
	@NotNull(message = "Otp is required.")
	private int otp;
}
