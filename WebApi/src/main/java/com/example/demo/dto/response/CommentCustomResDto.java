package com.example.demo.dto.response;

import java.sql.Timestamp;

public interface CommentCustomResDto {
	
	String getCommentId();
	
	String getFullName();
	
	String getContent();
	
	int getCountLikes();

	Timestamp getCreateTs();

	Timestamp getUpdateTs();

}
