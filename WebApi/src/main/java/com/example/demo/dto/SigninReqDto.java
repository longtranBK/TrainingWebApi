package com.example.demo.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class SigninReqDto {
	
	@NotBlank(message = "The username is required.")
    private String username;
    
	@NotBlank(message = "The password is required.")
    private String password;
    
}
