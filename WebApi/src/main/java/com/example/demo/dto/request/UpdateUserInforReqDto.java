package com.example.demo.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.example.demo.constant.Constants;

import lombok.Data;

@Data
public class UpdateUserInforReqDto {
	
	@Size(max = 128)
	@NotBlank(message = "The fullName is required.")
	private String fullName;
	
	@Size(max = 1)
	@NotNull(message = "The sex is required.")
	private String sex;
	
	@Size(max = 128)
	private String studyAt;
	
	@Size(max = 128)
	private String workingAt;
	
	@Size(max = 1024)
	private String favorites;
	
	@Size(max = 1024)
	private String otherInfor;
	
	@Size(max = 10)
	@NotBlank(message = "The dateOfBirth is required.")
	@Pattern(regexp = Constants.DATE_PATTERN, message = "The dateOfBirth is invalid.")
	private String dateOfBirth;
	
	@Size(max = 256)
	private String avatarUrl;
	
}
