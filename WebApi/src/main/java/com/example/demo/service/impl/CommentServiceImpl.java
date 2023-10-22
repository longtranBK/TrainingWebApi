package com.example.demo.service.impl;

import java.sql.Timestamp;
import java.util.UUID;

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
	public Comment insertComment(InsertCommentReqDto request, String userId) {
		String commentId = UUID.randomUUID().toString();
		
		Comment comment = new Comment();
		comment.setCommentId(commentId);
		comment.setPostId(request.getPostId());
		comment.setUserId(userId);
		comment.setContent(request.getContent());
		return commentRepository.save(comment);
		
	}

	@Override
	public Comment findByCommentIdAndUserId(String commentId, String userId) {
		return commentRepository.findByCommentIdAndUserId(commentId, userId);
	}

	@Override
	public Comment updateComment(Comment comment, String content) {
		comment.setContent(content);
		return commentRepository.save(comment);
	}

	@Override
	public void deleteComment(Comment comment) {
		commentRepository.delete(comment);
	}

}
