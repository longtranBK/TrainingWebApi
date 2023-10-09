package com.example.demo.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.example.demo.constant.Constants;

import lombok.Data;

@Data
public class UpdateUserInforReqDto {
	
	@NotBlank(message = "The fullName is required.")
	private String fullName;
	
	@NotBlank(message = "The avatarUrl is required.")
	private String avatarUrl;
	
	@NotNull(message = "The sex is required.")
	private int sex;
	
	@NotBlank(message = "The studyAt is required.")
	private String studyAt;
	
	@NotBlank(message = "The workingAt is required.")
	private String workingAt;
	
	@NotBlank(message = "The favorites is required.")
	private String favorites;
	
	@NotBlank(message = "The otherInfor is required.")
	private String otherInfor;
	
	@NotBlank(message = "The dateOfBirth is required.")
	@Pattern(regexp = Constants.DATE_PATTERN, message = "The dateOfBirth is invalid.")
	private String dateOfBirth;
	
}
