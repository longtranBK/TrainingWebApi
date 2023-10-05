package com.example.demo.controller;

import java.text.ParseException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.request.InsertCommentReqDto;
import com.example.demo.dto.request.UpdateCommentReqDto;
import com.example.demo.entity.Comment;
import com.example.demo.entity.Post;
import com.example.demo.service.CommentService;
import com.example.demo.service.PostService;

@RestController
@RequestMapping("api/comments")
public class CommentController {
	
	@Autowired
	private PostService postService;
	
	@Autowired
	private CommentService commentService;

	@PostMapping(value = { "" })
	public ResponseEntity<?> insertComment(@Valid @RequestBody InsertCommentReqDto request) {

		Post post = postService.findByPostId(request.getPostId());
		
		if (post == null) {
			return ResponseEntity.ok().body("Post not found!");
		}
		commentService.insertComment(request);
		
		return ResponseEntity.ok().body("Insert comment successful!");

	}

	@PutMapping(value = { "/{commentId}" })
	public ResponseEntity<?> updateComment(@PathVariable(value = "commentId") String commentId,
			@Valid @RequestBody UpdateCommentReqDto request) {

		Comment comment = commentService.findByCommentId(commentId);
		if (comment == null) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok().body("Update comment successful!");

	}

	@DeleteMapping(value = { "/{commentId}" })
	public ResponseEntity<?> deletePost(@PathVariable(value = "commentId") String commentId)
			throws ParseException {

		Comment comment = commentService.findByCommentId(commentId);

		if (comment == null) {
			return ResponseEntity.notFound().build();
		}

		commentService.deleteComment(comment);
		return ResponseEntity.ok().body("Delete comment successful!");
	}
}
