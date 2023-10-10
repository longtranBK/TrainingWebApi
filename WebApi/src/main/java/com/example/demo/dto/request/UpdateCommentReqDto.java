package com.example.demo.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class UpdateCommentReqDto {
	
	@Size(max = 36)
	@NotBlank(message = "The commentId is required.")
	private String commentId;

	@Size(max = 1024)
	@NotBlank(message = "The content is required.")
	private String content;
	
}
