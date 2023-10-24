package com.example.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.CommentLike;
import com.example.demo.entity.PostLike;
import com.example.demo.repository.CommentLikeRepository;
import com.example.demo.repository.PostLikeRepository;
import com.example.demo.service.ReactionService;
import com.example.demo.service.UserService;

@Service
public class ReactionServiceImpl implements ReactionService {

	@Autowired
	private PostLikeRepository postLikeRepository;

	@Autowired
	private CommentLikeRepository commentLikeRepository;

	@Autowired
	private UserService userService;

	@Override
	public PostLike likePost(String postId) {
		PostLike postLike = new PostLike();
		postLike.setPostId(postId);
		postLike.setUserId(userService.getUserId());
		return postLikeRepository.save(postLike);
	}

	@Override
	public void unlikePost(String postId) {
		PostLike postLike = postLikeRepository.findByPostIdAndUserId(postId, userService.getUserId());
		postLikeRepository.delete(postLike);
	}

	@Override
	public String reactionPost(String postId) {
		String userId = userService.getUserId();
		if (postLikeRepository.hasLike(userId, postId)) {
			unlikePost(postId);
			return "Unlike success!";
		} else {
			if (likePost(postId) != null) {
				return "Like success!";
			}
		}
		return "Reaction error!";
	}

	@Override
	public CommentLike likeComment(String commentId) {
		CommentLike commentLike = new CommentLike();
		commentLike.setCommentId(commentId);
		commentLike.setUserId(userService.getUserId());
		return commentLikeRepository.save(commentLike);
	}

	@Override
	public void unlikeComment(String commentId) {
		CommentLike commentLike = commentLikeRepository.findByCommentIdAndUserId(commentId, userService.getUserId());
		commentLikeRepository.delete(commentLike);
	}

	@Override
	public String reactionComment(String commentId) {
		String userId = userService.getUserId();
		if (commentLikeRepository.hasLike(userId, commentId)) {
			unlikeComment(commentId);
			return "Unlike success!";
		} else {
			if (likeComment(commentId) != null) {
				return "Like success!";
			}
		}
		return "Reaction error!";
	}

}
