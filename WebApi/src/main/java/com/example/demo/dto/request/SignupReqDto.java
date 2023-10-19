package com.example.demo.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.validation.annotation.Validated;

import lombok.Data;

@Data
@Validated
public class SignupReqDto {
	
	@Size(max = 100)
	@NotBlank(message = "Username is required.")
	private String username;
	
	@Size(max = 100)
	@Email
	@NotBlank(message = "Email is required.")
	private String email;
	
	@Size(max = 20)
	@NotBlank(message = "Password is required.")
	private String password;

}
