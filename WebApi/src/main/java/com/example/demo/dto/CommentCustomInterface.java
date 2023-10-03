package com.example.demo.dto;

import java.sql.Timestamp;

public interface CommentCustomInterface {

	String getCommentId();

	String getContent();

	Timestamp getCreateTs();

	Timestamp getUpdateTs();

}
