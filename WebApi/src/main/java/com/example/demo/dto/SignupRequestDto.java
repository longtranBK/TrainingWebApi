package com.example.demo.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.springframework.validation.annotation.Validated;

import lombok.Data;

@Data
@Validated
public class SignupRequestDto {
	
	@NotBlank(message = "The fullName is required.")
	private String fullName;
	
	private String avatarUrl;
	
	@NotBlank(message = "The username is required.")
	private String username;
	
	@NotBlank(message = "The password is required.")
	private String password;
	
	@Email
	@NotBlank(message = "The email is required.")
	private String email;
	
	@NotBlank(message = "The dateOfBirth is required.")
	@Pattern(regexp = "^((2000|2400|2800|(19|2[0-9])(0[48]|[2468][048]|[13579][26]))-02-29)$"
			+ "|^(((19|2[0-9])[0-9]{2})-02-(0[1-9]|1[0-9]|2[0-8]))$"
			+ "|^(((19|2[0-9])[0-9]{2})-(0[13578]|10|12)-(0[1-9]|[12][0-9]|3[01]))$"
			+ "|^(((19|2[0-9])[0-9]{2})-(0[469]|11)-(0[1-9]|[12][0-9]|30))$", message = "The dateOfBirth is invalid.")
	private String dateOfBirth;
}
