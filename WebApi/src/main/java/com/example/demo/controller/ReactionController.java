package com.example.demo.controller;

import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Comment;
import com.example.demo.entity.Post;
import com.example.demo.service.CommentService;
import com.example.demo.service.PostService;
import com.example.demo.service.ReactionService;
import com.example.demo.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Reaction", description = "API reaction với post, like")
@Validated
@RestController
@RequestMapping("/v1/")
public class ReactionController {

	@Autowired
	private PostService postService;
	
	@Autowired
	private CommentService commentService;

	@Autowired
	private UserService userService;

	@Autowired
	private ReactionService reactionService;
	
	@Operation(summary = "Reaction post")
	@PutMapping(value = "/reaction-post/{postId}")
	public ResponseEntity<?> reactionPost(
			@PathVariable(value = "postId", required = true) @Size(max = 36) String postId) {
		String userId = userService.getUserId();
		Post post = postService.findByPostIdAndUserId(postId, userId);
		if (post == null) {
			return ResponseEntity.ok().body("Post not found!");
		}

		return ResponseEntity.ok().body(reactionService.reactionPost(postId));
	}

	@Operation(summary = "Reaction comment")
	@PutMapping(value = "/reaction-comment/{commentId}")
	public ResponseEntity<?> reactionComment(
			@PathVariable(value = "commentId", required = true) @Size(max = 36) String commentId) {
		String userId = userService.getUserId();
		Comment comment = commentService.findByCommentIdAndUserId(commentId, userId);
		if (comment == null) {
			return ResponseEntity.ok().body("Comment not found!");
		}

		return ResponseEntity.ok().body(reactionService.reactionComment(commentId));
	}
}
