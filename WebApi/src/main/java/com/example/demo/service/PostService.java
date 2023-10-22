package com.example.demo.service;


import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.request.InsertPostReqDto;
import com.example.demo.dto.request.UpdatePostReqDto;
import com.example.demo.dto.response.GetPostResDto;
import com.example.demo.dto.response.PostUpdateResDto;
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
	 * @param imageList
	 * @param userId
	 * @return Post if insert success
	 */
	Post insertPost(InsertPostReqDto request, MultipartFile[] imageList, String userId);
	
	/**
	 * Get all post of me and friend
	 * 
	 * @param startDate
	 * @param endDate
	 * @param limitPost
	 * @param offsetPost
	 * @param limitComment
	 * @param offsetComment
	 * @return GetPostResDto list
	 */
	List<GetPostResDto> getAllPost(Date startDate, Date endDate, int limitPost, int offsetPost, int limitComment, int offsetComment);
	
	/**
	 * Update post
	 * 
	 * @param post
	 * @param request
	 * @param avatarList
	 * @return PostUpdateResDto if update success
	 */
	PostUpdateResDto updatePost(Post post, UpdatePostReqDto request, MultipartFile[] avatarList);
	
	/**
	 * Delete post
	 * 
	 * @param postId
	 */
	void deletePost(String postId);

	
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
