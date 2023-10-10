package com.example.demo.dto.response;

import java.sql.Timestamp;

public interface CommentCustomResDto {
	
	String getCommentId();
	
	String getUserId();

	String getContent();

	Timestamp getCreateTs();

	Timestamp getUpdateTs();

}
