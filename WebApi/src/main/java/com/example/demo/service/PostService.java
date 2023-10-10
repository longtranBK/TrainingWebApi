package com.example.demo.service;

import java.sql.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.request.InsertPostReqDto;
import com.example.demo.dto.request.UpdatePostReqDto;
import com.example.demo.dto.response.GetPostResDto;
import com.example.demo.entity.Post;

public interface PostService {
	
	/**
	 * Find post
	 * 
	 * @param postId
	 * @return post
	 */
	Post findByPostId(String postId);
	
	/**
	 * Find post
	 * 
	 * @param postId
	 * @return post
	 */
	Post findByPostIdAndUserId(String postId, String userId);
	
	/**
	 * Insert post
	 * 
	 * @param request
	 * @param avatarList
	 * @param userId
	 */
	void insertPost(InsertPostReqDto request, MultipartFile[] avatarList, String userId);
	
	/**
	 * Find post with condition
	 * 
	 * @param userId
	 * @param startDate
	 * @param endDate
	 * @param numbersPost
	 * @return list GetPostResDto
	 */
	List<GetPostResDto> getPostCustom(String userId, Date startDate, Date endDate, int numbersPost);
	
	/**
	 * Update post
	 * 
	 * @param post
	 * @param request
	 * @param avatarList
	 */
	void updatePost(Post post, UpdatePostReqDto request, MultipartFile[] avatarList);
	
	/**
	 * Delete post
	 * 
	 * @param post
	 */
	void deletePost(Post post);
	
	/**
	 * Get post on time line
	 * 
	 * @param numbersPost
	 * @return
	 */
	List<GetPostResDto> getPostTimeLine(int numbersPost, String userId);
	
	/**
	 * Check lisk of post
	 * 
	 * @param userId
	 * @param postId
	 * @return true if liked
	 */
	boolean hasLike(String userId, String postId);
	
	/**
	 * Like post
	 * 
	 * @param userId
	 * @param postId
	 */
	void likePost(String userId, String postId);
	
	/**
	 * 
	 * Dislike post
	 * @param userId
	 * @param postId
	 */
	void dislikePost(String userId, String postId);
}
