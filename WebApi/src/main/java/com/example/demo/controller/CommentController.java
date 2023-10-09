package com.example.demo.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.constant.Constants;
import com.example.demo.dto.request.DeleteCommentReqDto;
import com.example.demo.dto.request.InsertCommentReqDto;
import com.example.demo.dto.request.UpdateCommentReqDto;
import com.example.demo.entity.Comment;
import com.example.demo.entity.Post;
import com.example.demo.service.CommentService;
import com.example.demo.service.PostService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Comment", description = "API thao tác với comment")
@RestController
@RequestMapping("api/comments")
public class CommentController {

	@Autowired
	private PostService postService;

	@Autowired
	private CommentService commentService;

	@Operation(summary = "Comment to post")
	@PostMapping(value = { "" })
	@Secured(Constants.ROLE_USER_NAME)
	public ResponseEntity<?> insertComment(@Valid @RequestBody InsertCommentReqDto request) {

		Post post = postService.findByPostId(request.getPostId());
		if (post == null) {
			return ResponseEntity.ok().body("Post not found!");
		}
		commentService.insertComment(request);

		return ResponseEntity.ok().body("Insert comment successful!");

	}

	@Operation(summary = "Edit comment in post")
	@PutMapping(value = { "" })
	@Secured(Constants.ROLE_USER_NAME)
	public ResponseEntity<?> updateComment(@Valid @RequestBody UpdateCommentReqDto request) {

		Comment comment = commentService.findByUserIdAndPostId(request.getUserId(), request.getPostId());
		if (comment == null) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok().body("Update comment successful!");
	}

	@Operation(summary = "Delete comment in post")
	@DeleteMapping(value = { "" })
	@Secured(Constants.ROLE_USER_NAME)
	public ResponseEntity<?> deleteComment(@Valid @RequestBody DeleteCommentReqDto request) {

		Comment comment = commentService.findByUserIdAndPostId(request.getUserId(), request.getPostId());
		if (comment == null) {
			return ResponseEntity.notFound().build();
		}

		commentService.deleteComment(comment);
		return ResponseEntity.ok().body("Delete comment successful!");
	}
}
