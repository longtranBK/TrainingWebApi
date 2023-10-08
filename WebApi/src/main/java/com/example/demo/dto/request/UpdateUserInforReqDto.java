package com.example.demo.dto.request;

import javax.validation.constraints.Pattern;

import com.example.demo.constant.Constants;

import lombok.Data;

@Data
public class UpdateUserInforReqDto {
	
	private String fullName;
	
	private String avatarUrl;
	
	private int sex;
	
	private String studyAt;
	
	private String workingAt;
	
	private String favorites;
	
	private String otherInfor;
	
	@Pattern(regexp = Constants.DATE_PATTERN, message = "The dateOfBirth is invalid.")
	private String dateOfBirth;
	
}
