package com.example.demo.dto.request;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class InsertCommentReqDto {

	@NotBlank(message = "The userId is required.")
	private String userId;
	
	@NotBlank(message = "The postId is required.")
	private String postId;
	
	@NotBlank(message = "The content is required.")
	private String content;
}
