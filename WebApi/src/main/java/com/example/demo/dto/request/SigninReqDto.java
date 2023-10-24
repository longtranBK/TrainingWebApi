package com.example.demo.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.example.demo.constant.Constants;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SigninReqDto {
	
	@Schema(type = "string", example = "longth")
	@Size(max = 100)
	@NotBlank(message = "The username is required.")
    private String username;
    
	@Schema(type = "string", example = "Longth@12345")
	@Size(min = 8, max = 20)
	@NotBlank(message = "The password is required.")
	@Pattern(regexp = Constants.PASSWORD_PATTERN, message = Constants.MESSAGE_REGEX_PASSWORD)
    private String password;
    
}
