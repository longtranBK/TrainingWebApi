package com.example.demo.dto.request;

import lombok.Data;

@Data
public class UpdateCommentReqDto {
	
	private String userId;
	
	private String postId;

	private String content;
	
}
