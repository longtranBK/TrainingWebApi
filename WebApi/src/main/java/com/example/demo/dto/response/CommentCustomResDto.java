package com.example.demo.dto.response;

import java.sql.Timestamp;

public interface CommentCustomResDto {
	
	String getUserId();

	String getContent();

	Timestamp getCreateTs();

	Timestamp getUpdateTs();

}
