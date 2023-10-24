package com.example.demo.controller;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.request.InsertCommentReqDto;
import com.example.demo.dto.request.UpdateCommentReqDto;
import com.example.demo.dto.response.GetPostResDto;
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
	public ResponseEntity<?> insertComment(
			@Valid @RequestBody(required = true) InsertCommentReqDto request) {

		Post post = postService.findByPostId(request.getPostId());
		if (post == null) {
			return ResponseEntity.ok().body("Post not found!");
		}
		if (commentService.insertComment(request) != null) {
			return ResponseEntity.ok().body("Insert comment successful!");
		} else {
			return ResponseEntity.internalServerError().body("Insert comment error!");
		}
	}

	@Operation(summary = "Edit comment in post")
	@PutMapping(value = "/{commentId}")
	public ResponseEntity<?> updateComment(
			@PathVariable(required = true) @Size(max = 36) String commentId,
			@Valid @RequestBody(required = true) UpdateCommentReqDto request) {

		Comment comment = commentService.findByCommentIdAndUserId(commentId, userService.getUserId());
		if (comment == null) {
			return ResponseEntity.notFound().build();
		}

		Comment commentRes = commentService.updateComment(comment, request.getContent());
		if (commentRes != null) {
			return ResponseEntity.ok().body(commentRes);
		} else {
			return ResponseEntity.internalServerError().body("Update comment error!");
		}
	}

	@Operation(summary = "Delete comment in post")
	@DeleteMapping(value = "/{commentId}")
	public ResponseEntity<?> deleteComment(
			@PathVariable(required = true) @Size(max = 36) String commentId) {

		Comment comment = commentService.findByCommentIdAndUserId(commentId, userService.getUserId());
		if (comment == null) {
			return ResponseEntity.notFound().build();
		}
		commentService.deleteComment(commentId);
		return ResponseEntity.ok().body("Delete comment successful!");
	}
	
	@Operation(summary = "Get comment of post")
	@GetMapping
	public @ResponseBody ResponseEntity<?> getPost( 
			@RequestParam(value = "postId", required = true) @Size(max = 36) String postId,
			@RequestParam(value = "limit", required = true) int limit,
			@RequestParam(value = "offset", required = true) int offset) {
		Post post = postService.findByPostId(postId);
		if (post == null) {
			return ResponseEntity.ok().body("Post not found!");
		}
		
		return ResponseEntity.ok().body(commentService.getCommentOfPost(postId, limit, offset));
	}
}
