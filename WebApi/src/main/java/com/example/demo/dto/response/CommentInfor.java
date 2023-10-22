package com.example.demo.dto.response;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class CommentInfor {
	
	private String commentId;
	
	private String userId;

	private String content;
	
	private int countLike;

	private Timestamp createTs;

	private Timestamp updateTs;
}
