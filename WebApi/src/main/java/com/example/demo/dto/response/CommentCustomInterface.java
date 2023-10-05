package com.example.demo.dto.response;

import java.sql.Timestamp;

public interface CommentCustomInterface {

	String getCommentId();
	
	String getUserId();

	String getContent();

	Timestamp getCreateTs();

	Timestamp getUpdateTs();

}
