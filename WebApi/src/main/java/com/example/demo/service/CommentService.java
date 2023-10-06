package com.example.demo.service;

import com.example.demo.dto.request.InsertCommentReqDto;
import com.example.demo.entity.Comment;

public interface CommentService {

	void insertComment(InsertCommentReqDto request);
	
	Comment findByUserIdAndPostId(String userId, String postId);
	
	void updateComment(Comment comment, String content);
	
	void deleteComment(Comment comment);
}
