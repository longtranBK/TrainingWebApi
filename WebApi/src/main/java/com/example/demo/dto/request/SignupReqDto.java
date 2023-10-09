package com.example.demo.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.validation.annotation.Validated;

import com.example.demo.constant.Constants;

import lombok.Data;

@Data
@Validated
public class SignupReqDto {
	
	@NotBlank(message = "FullName is required.")
	private String fullName;
	
	@Email
	@NotBlank(message = "Username is required.")
	private String username;
	
	@NotBlank(message = "Password is required.")
	private String password;
	
	@NotNull(message = "Sex is required.")
	private int sex;
	
	@NotBlank(message = "Birthday is required.")
	@Pattern(regexp = Constants.DATE_PATTERN, message = "Birthday is invalid.")
	private String dateOfBirth;
}
