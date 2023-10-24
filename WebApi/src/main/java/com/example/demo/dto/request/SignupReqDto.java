package com.example.demo.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.example.demo.constant.Constants;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SignupReqDto {

	@Schema(type = "string", example = "longth")
	@Size(max = 100)
	@NotBlank(message = "Username is required.")
	private String username;

	@Schema(type = "string", example = "longth@gmail.com")
	@Size(max = 100)
	@Email(regexp = Constants.EMAIL_PATTERN, message = "Invalid email address!")
	@NotBlank(message = "Email is required.")
	private String email;

	@Schema(type = "string", example = "Longth@12345")
	@Size(min = 8, max = 20)
	@NotBlank(message = "Password is required.")
	@Pattern(regexp = Constants.PASSWORD_PATTERN, message = Constants.MESSAGE_REGEX_PASSWORD)
	private String password;

}
