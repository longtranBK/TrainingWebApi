package com.example.demo.service.impl;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.request.InsertCommentReqDto;
import com.example.demo.entity.Comment;
import com.example.demo.repository.CommentRepository;
import com.example.demo.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService{

	@Autowired
	private CommentRepository commentRepository;
	
	@Override
	public void insertComment(InsertCommentReqDto request) {
		Timestamp upadteTs = new java.sql.Timestamp(System.currentTimeMillis());

		Comment comment = new Comment();
		comment.setPostId(request.getPostId());
		comment.setUserId(request.getUserId());
		comment.setContent(request.getContent());
		comment.setCreateTs(upadteTs);
		comment.setUpdateTs(upadteTs);
		commentRepository.save(comment);
		
	}

	@Override
	public Comment findByUserIdAndPostId(String userId, String postId) {
		return commentRepository.findByUserIdAndPostId(userId, postId);
	}

	@Override
	public void updateComment(Comment comment, String content) {
		Timestamp upadteTs = new java.sql.Timestamp(System.currentTimeMillis());

		comment.setContent(content);
		comment.setUpdateTs(upadteTs);

		commentRepository.save(comment);
		
	}

	@Override
	public void deleteComment(Comment comment) {
		commentRepository.delete(comment);
	}

}
