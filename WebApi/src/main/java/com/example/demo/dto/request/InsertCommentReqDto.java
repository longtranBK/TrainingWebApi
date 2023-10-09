package com.example.demo.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class InsertCommentReqDto {

	@Size(max = 36)
	@NotBlank(message = "The userId is required.")
	private String userId;
	
	@Size(max = 36)
	@NotBlank(message = "The postId is required.")
	private String postId;
	
	@Size(max = 1024)
	@NotBlank(message = "The content is required.")
	private String content;
}
