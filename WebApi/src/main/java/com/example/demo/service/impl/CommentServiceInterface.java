package com.example.demo.service.impl;

import com.example.demo.dto.request.InsertCommentReqDto;
import com.example.demo.entity.Comment;

public interface CommentServiceInterface {

	void insertComment(InsertCommentReqDto request);
	
	Comment findByCommentId(String commentId);
	
	void updateComment(Comment comment, String content);
	
	void deleteComment(Comment comment);
}
