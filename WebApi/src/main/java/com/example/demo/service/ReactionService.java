package com.example.demo.service;

import com.example.demo.entity.CommentLike;
import com.example.demo.entity.PostLike;

public interface ReactionService {

	/**
	 * Like post
	 * 
	 * @param postId
	 * @return
	 */
	PostLike likePost(String postId);
	
	/**
	 * Unlike post
	 * 
	 * @param postId
	 */
	void unlikePost(String postId);
	
	/**
	 * Reaction post
	 * 
	 * @param postId
	 * @return
	 */
	String reactionPost(String postId);
	
	/**
	 * Like comment
	 * 
	 * @param commentId
	 * @return
	 */
	CommentLike likeComment(String commentId);
	
	/**
	 * Unlike comment
	 * 
	 * @param commentId
	 */
	void unlikeComment(String commentId);
	
	/**
	 * Reaction comment
	 * 
	 * @param commentId
	 * @return
	 */
	String reactionComment(String commentId);
}
