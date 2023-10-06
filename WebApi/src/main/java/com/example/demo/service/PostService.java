package com.example.demo.service;

import java.sql.Date;
import java.util.List;

import com.example.demo.dto.request.InsertPostReqDto;
import com.example.demo.dto.request.UpdatePostReqDto;
import com.example.demo.dto.response.GetPostResDto;
import com.example.demo.entity.Post;

public interface PostService {
	
	Post findByPostId(String postId);
	
	void insertPost(InsertPostReqDto request);
	
	List<GetPostResDto> getPostCustom(String userId, Date startDate, Date endDate, int numbersPost);
	
	void updatePost(Post post, UpdatePostReqDto request);
	
	void deletePost(Post post);
	
	List<GetPostResDto> getPostTimeLine(int numbersPost);
	
	boolean hasLike(String userId, String postId);
	
	void likePost(String userId, String postId);
	
	void dislikePost(String userId, String postId);
}
