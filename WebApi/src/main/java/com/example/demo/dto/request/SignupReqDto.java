package com.example.demo.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.validation.annotation.Validated;

import com.example.demo.constant.Constants.ValidatePattern;

import lombok.Data;

@Data
@Validated
public class SignupReqDto {
	
	@NotBlank(message = "The fullName is required.")
	private String fullName;
	
	private String avatarUrl;
	
	@NotBlank(message = "The username is required.")
	private String username;
	
	@NotBlank(message = "The password is required.")
	private String password;
	
	@NotNull(message = "The sex is required.")
	private int sex;
	
	@Email
	@NotBlank(message = "The email is required.")
	private String email;
	
	@NotBlank(message = "The dateOfBirth is required.")
	@Pattern(regexp = ValidatePattern.DATE_PATTERN, message = "The dateOfBirth is invalid.")
	private String dateOfBirth;
}
