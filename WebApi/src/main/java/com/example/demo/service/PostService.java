package com.example.demo.service;

import java.sql.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.request.InsertPostReqDto;
import com.example.demo.dto.request.UpdatePostReqDto;
import com.example.demo.dto.response.GetPostResDto;
import com.example.demo.entity.Post;
import com.example.demo.entity.PostLike;

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
	 * Insert new post
	 * 
	 * @param request
	 * @param avatarList
	 * @param userId
	 * @return Post if insert success
	 */
	Post insertPost(InsertPostReqDto request, MultipartFile[] avatarList, String userId);
	
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
	 * @return Post if update success
	 */
	Post updatePost(Post post, UpdatePostReqDto request, MultipartFile[] avatarList);
	
	/**
	 * Delete post
	 * 
	 * @param post
	 */
	void deletePost(Post post);
	
	/**
	 * Get post timeline
	 * 
	 * @param userId
	 * @param numbersPost
	 * @return List<GetPostResDto>
	 */
	List<GetPostResDto> getPostTimeline(String userId, int numbersPost);
	
	/**
	 * Check like of post
	 * 
	 * @param userId
	 * @param postId
	 * @return true if liked
	 */
	boolean hasLike(String userId, String postId);
	
	/**
	 * Like a post
	 * 
	 * @param userId
	 * @param postId
	 * @return true if like success
	 */
	PostLike likePost(String userId, String postId);
	
	/**
	 * Dislike a post
	 * 
	 * Dislike post
	 * @param userId
	 * @param postId
	 */
	void dislikePost(String userId, String postId);
}
