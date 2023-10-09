package com.example.demo.dto.request;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class LikePostReqDto {
	
	@NotBlank(message = "The userId is required.")
	private String userId;
	
	@NotBlank(message = "The postId is required.")
	private String postId;

}
