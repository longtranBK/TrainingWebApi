package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.request.InsertCommentReqDto;
import com.example.demo.dto.response.CommentCustomResDto;
import com.example.demo.entity.Comment;

public interface CommentService {

	/**
	 * Insert comment
	 * 
	 * @param request
	 * @return Comment if insert success
	 */
	Comment insertComment(InsertCommentReqDto request);
	
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
	 * @return Comment if update success
	 */
	Comment updateComment(Comment comment, String content);
	
	/**
	 * Delete comment
	 * 
	 * @param commentId
	 */
	void deleteComment(String commentId);
	
	/**
	 * 
	 * @param postId
	 * @param limit
	 * @param offset
	 * @return
	 */
	List<CommentCustomResDto> getCommentOfPost(String postId, int limit, int offset);
}
