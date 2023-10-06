package com.example.demo.dto.request;

import lombok.Data;

@Data
public class DeleteCommentReqDto {
	
	private String userId;
	
	private String postId;
	
}
