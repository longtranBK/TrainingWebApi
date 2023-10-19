package com.example.demo.controller;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.constant.Constants;
import com.example.demo.dto.request.InsertCommentReqDto;
import com.example.demo.dto.request.UpdateCommentReqDto;
import com.example.demo.entity.Comment;
import com.example.demo.entity.Post;
import com.example.demo.service.CommentService;
import com.example.demo.service.PostService;
import com.example.demo.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Comment", description = "API thao tác với comment")
@Validated
@RestController
@RequestMapping("/v1/comments")
public class CommentController {

	@Autowired
	private PostService postService;

	@Autowired
	private CommentService commentService;

	@Autowired
	private UserService userService;

	@Operation(summary = "Comment to post")
	@PostMapping
	@Secured(Constants.ROLE_USER_NAME)
	public ResponseEntity<?> insertComment(
			@Valid @RequestBody(required = true) InsertCommentReqDto request) {

		Post post = postService.findByPostId(request.getPostId());
		if (post == null) {
			return ResponseEntity.ok().body("Post not found!");
		}
		if (commentService.insertComment(request, userService.getUserId()) != null) {
			return ResponseEntity.ok().body("Insert comment successful!");
		} else {
			return ResponseEntity.internalServerError().body("Insert comment error!");
		}
	}

	@Operation(summary = "Edit comment in post")
	@PutMapping
	@Secured(Constants.ROLE_USER_NAME)
	public ResponseEntity<?> updateComment(
			@Valid @RequestBody(required = true) UpdateCommentReqDto request) {

		Comment comment = commentService.findByCommentIdAndUserId(request.getCommentId(), userService.getUserId());
		if (comment == null) {
			return ResponseEntity.notFound().build();
		}

		if (commentService.updateComment(comment, request.getContent()) != null) {
			return ResponseEntity.ok().body("Update comment successful!");
		} else {
			return ResponseEntity.internalServerError().body("Update comment error!");
		}
	}

	@Operation(summary = "Delete comment in post")
	@DeleteMapping(value = { "/{commentId}" })
	@Secured(Constants.ROLE_USER_NAME)
	public ResponseEntity<?> deleteComment(
			@PathVariable(required = true) @Size(max = 36) String commentId) {

		Comment comment = commentService.findByCommentIdAndUserId(commentId, userService.getUserId());
		if (comment == null) {
			return ResponseEntity.notFound().build();
		}

		commentService.deleteComment(comment);
		return ResponseEntity.ok().body("Delete comment successful!");
	}
}
