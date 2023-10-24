package com.example.demo.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.request.InsertCommentReqDto;
import com.example.demo.dto.response.CommentCustomResDto;
import com.example.demo.entity.Comment;
import com.example.demo.repository.CommentRepository;
import com.example.demo.service.CommentService;
import com.example.demo.service.UserService;

@Service
public class CommentServiceImpl implements CommentService{

	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private UserService userService;
	
	@Override
	public Comment insertComment(InsertCommentReqDto request) {
		String userId =  userService.getUserId();
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
		return commentRepository.findByCommentIdAndUserIdAndDelFlg(commentId, userId, false);
	}

	@Override
	public Comment updateComment(Comment comment, String content) {
		comment.setContent(content);
		return commentRepository.save(comment);
	}

	@Override
	public void deleteComment(String commentId) {
		commentRepository.updateDelFlg(commentId, false);
	}

	@Override
	public List<CommentCustomResDto> getCommentOfPost(String postId, int limit, int offset) {
		return commentRepository.findByPostIdCustom(postId, limit, offset);
	}

}
