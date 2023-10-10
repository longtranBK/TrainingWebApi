package com.example.demo.service;

import com.example.demo.dto.request.InsertCommentReqDto;
import com.example.demo.entity.Comment;

public interface CommentService {

	/**
	 * Insert comment
	 * 
	 * @param request
	 */
	void insertComment(InsertCommentReqDto request);
	
	/**
	 * Search comment
	 * 
	 * @param commentId
	 * @return comment
	 */
	Comment findByCommentId(String commentId);
	
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
