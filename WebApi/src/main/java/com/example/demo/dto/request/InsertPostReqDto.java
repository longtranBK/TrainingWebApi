package com.example.demo.dto.request;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class InsertPostReqDto {
	
	@NotBlank(message = "The user id is required.")
	private String userId;
	
	@NotBlank(message = "The content is required.")
	private String content;
	
	@NotNull(message = "The status is required.")
	private int status;
	
	private List<String> capturesUrl;
}
