package com.example.demo.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.example.demo.constant.Constants.ValidatePattern;

import lombok.Data;

@Data
public class UpdateUserInforReqDto {

	@NotBlank(message = "The user id is required.")
	private String id;
	
	private String fullName;
	
	private String avatarUrl;
	
	private int sex;
	
	private String studyAt;
	
	private String workingAt;
	
	private String favorites;
	
	private String otherInfor;
	
	@Pattern(regexp = ValidatePattern.DATE_PATTERN, message = "The dateOfBirth is invalid.")
	private String dateOfBirth;
}
