package com.example.demo.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class InsertPostReqDto {
	
	@Size(max = 5000)
	@NotBlank(message = "The content is required.")
	private String content;
	
	@Size(max = 1)
	@NotBlank(message = "The status is required.")
	private String status;
}
