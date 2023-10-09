package com.example.demo.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.validation.annotation.Validated;

import com.example.demo.constant.Constants;

import lombok.Data;

@Data
@Validated
public class SignupReqDto {
	
	@Size(max = 128)
	@NotBlank(message = "FullName is required.")
	private String fullName;
	
	@Size(max = 100)
	@Email
	@NotBlank(message = "Username is required.")
	private String username;
	
	@Size(max = 20)
	@NotBlank(message = "Password is required.")
	private String password;
	
	@Min(0)
	@Max(1)
	@NotNull(message = "Sex is required.")
	private int sex;
	
	@Size(max = 10)
	@NotBlank(message = "Birthday is required.")
	@Pattern(regexp = Constants.DATE_PATTERN, message = "Birthday is invalid.")
	private String dateOfBirth;
}
