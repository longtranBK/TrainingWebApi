package com.example.demo.service;

import com.example.demo.dto.request.InsertCommentReqDto;
import com.example.demo.entity.Comment;

public interface CommentService {

	/**
	 * Insert comment
	 * 
	 * @param request
	 * @param userId
	 */
	void insertComment(InsertCommentReqDto request, String userId);
	
	/**
	 * Search comment
	 * 
	 * @param commentId
	 * @param userId
	 * @return comment
	 */
	Comment findByCommentIdAndUserId(String commentId, String userId);
	
	/**
	 * Update comment
	 * 
	 * @param comment
	 * @param content
	 */
	void updateComment(Comment comment, String content);
	
	/**
	 * Delete comment
	 * 
	 * @param comment
	 */
	void deleteComment(Comment comment);
}
