package com.example.demo.controller;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.UUID;

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
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PostRepository;

@RestController
@RequestMapping("api/comments")
public class CommentController {

	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private CommentRepository commentRepository;

	@PostMapping(value = { "" })
	public ResponseEntity<?> insertComment(@Valid @RequestBody InsertCommentReqDto request) {

		Post post = postRepository.findByPostId(request.getPostId());

		if (post == null) {
			return ResponseEntity.ok().body("Post not found!");
		}

		String commentId = UUID.randomUUID().toString();
		Timestamp upadteTs = new java.sql.Timestamp(System.currentTimeMillis());

		Comment comment = new Comment();
		comment.setCommentId(commentId);
		comment.setPostId(request.getPostId());
		comment.setUserId(request.getUserId());
		comment.setContent(request.getContent());
		comment.setCreateTs(upadteTs);
		comment.setUpdateTs(upadteTs);

		commentRepository.save(comment);

		return ResponseEntity.ok().body("Insert comment successful!");

	}

	@PutMapping(value = { "/{commentId}" })
	public ResponseEntity<?> updateComment(@PathVariable(value = "commentId") String commentId,
			@Valid @RequestBody UpdateCommentReqDto request) {

		Comment comment = commentRepository.findByCommentId(commentId);

		if (comment == null) {
			return ResponseEntity.notFound().build();
		}

		Timestamp upadteTs = new java.sql.Timestamp(System.currentTimeMillis());

		comment.setContent(request.getContent());
		comment.setUpdateTs(upadteTs);

		commentRepository.save(comment);

		return ResponseEntity.ok().body("Update comment successful!");

	}

	@DeleteMapping(value = { "/{commentId}" })
	public ResponseEntity<?> deletePost(@PathVariable(value = "commentId") String commentId)
			throws ParseException {

		Comment comment = commentRepository.findByCommentId(commentId);

		if (comment == null) {
			return ResponseEntity.notFound().build();
		}

		// Delete table comment
		commentRepository.deleteByCommentId(commentId);

		return ResponseEntity.ok().body("Delete comment successful!");
	}
}
