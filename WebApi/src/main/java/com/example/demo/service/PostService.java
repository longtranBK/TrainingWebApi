package com.example.demo.service;


import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.request.InsertPostReqDto;
import com.example.demo.dto.request.UpdatePostReqDto;
import com.example.demo.dto.response.GetPostResDto;
import com.example.demo.dto.response.PostUpdateResDto;
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
	 * Get post of me or friend
	 * 
	 * @param postId
	 * @param limitComment
	 * @param offsetComment
	 * @return GetPostResDto list
	 */
	GetPostResDto getPost(String postId, int limitComment, int offsetComment);
	
}
