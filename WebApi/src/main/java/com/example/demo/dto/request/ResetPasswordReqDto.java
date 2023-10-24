package com.example.demo.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.example.demo.constant.Constants;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ResetPasswordReqDto {

	@Schema(type = "string", example = "307dd00d-7c25-46de-a8b4-c96e17996e39")
	@Size(max = 100)
	@NotBlank(message = "The username is required.")
	private String username;

	@Schema(type = "string", example = "307dd00d-7c25-46de-a8b4-c96e17996e39")
	@Size(max = 20)
	@Pattern(regexp = Constants.PASSWORD_PATTERN, message = Constants.MESSAGE_REGEX_PASSWORD)
	@NotBlank(message = "The newPassword is required.")
	private String newPassword;

	@Schema(type = "string", example = "TY5ODIwMzgwOX0.DfNEGgSkfTs5UI5DQsAiQw7rZ4Cx8i3XqQ7I1S1Bqhw")
	@Size(max = 20)
	@NotBlank(message = "The token is required.")
	private String token;
}
