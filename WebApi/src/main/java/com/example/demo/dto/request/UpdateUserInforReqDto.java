package com.example.demo.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.example.demo.constant.Constants;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UpdateUserInforReqDto {
	
	@Schema(type = "string", example = "Full name")
	@Size(max = 128)
	@NotBlank(message = "The fullName is required.")
	private String fullName;
	
	@Schema(type = "string", example = "0")
	@Size(max = 1)
	private String sex;
	
	@Schema(type = "string", example = "Study at Ha Noi")
	@Size(max = 128)
	private String studyAt;
	
	@Schema(type = "string", example = "Working at Ha Noi")
	@Size(max = 128)
	private String workingAt;
	
	@Schema(type = "string", example = "Soccer, football")
	@Size(max = 1024)
	private String favorites;
	
	@Schema(type = "string", example = "Movie, music")
	@Size(max = 1024)
	private String otherInfor;
	
	@Schema(type = "string", example = "2000-01-01")
	@Size(max = 10)
	@NotBlank(message = "The dateOfBirth is required.")
	@Pattern(regexp = Constants.DATE_PATTERN, message = "The dateOfBirth is invalid.")
	private String dateOfBirth;
	
	@Schema(type = "string", example = "D:/uploads/73b0ed55-5cde-45b8-bcb6-4258870387d2/Untitled.png")
	@Size(max = 256)
	private String avatarUrl;
	
}
