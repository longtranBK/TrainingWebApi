package com.example.demo.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class SigninReqDto {
	
	@Size(max = 100)
	@NotBlank(message = "The username is required.")
    private String username;
    
	@Size(max = 20)
	@NotBlank(message = "The password is required.")
    private String password;
    
}
