package com.example.demo.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.request.InsertPostReqDto;
import com.example.demo.dto.request.UpdatePostReqDto;
import com.example.demo.dto.response.GetPostResDto;
import com.example.demo.dto.response.PostUpdateResDto;
import com.example.demo.entity.Post;
import com.example.demo.service.PostService;
import com.example.demo.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Post", description = "API thao tác với post")
@Validated
@RestController
@RequestMapping("/v1/posts/")
public class PostController {

	@Autowired
	private UserService userService;

	@Autowired
	private PostService postService;

	@Operation(summary = "Insert new post")
	@PostMapping(consumes = "multipart/form-data")
	public ResponseEntity<?> insertPost(
			@Valid @RequestPart(value = "request", required = true) InsertPostReqDto request,
			@RequestPart(value = "files", required = false)  MultipartFile[] avatarFiles) {
		if (postService.insertPost(request, avatarFiles, userService.getUserId()) != null) {
			return ResponseEntity.ok().body("Insert post successful!");
		} else {
			return ResponseEntity.internalServerError().body("Insert post error!");
		}
	}
	
	@Operation(summary = "Update post")
	@PutMapping(value = "/{postId}", consumes = "multipart/form-data")
	public ResponseEntity<?> updatePost(
			@PathVariable(value = "postId", required = true) @Size(max = 36) String postId,
			@Valid @RequestPart(value = "request", required = true) UpdatePostReqDto request, 
			@RequestPart(value = "files", required = false) MultipartFile[] avatarFiles)
			throws ParseException {

		Post post = postService.findByPostIdAndUserId(postId, userService.getUserId());
		if (post == null) {
			return ResponseEntity.ok().body("Post not found!");
		}

		PostUpdateResDto res = postService.updatePost(post, request, avatarFiles);
		if(res != null) {
			return ResponseEntity.ok().body(res);
		} else {
			return ResponseEntity.internalServerError().body("Update post error!");
		}
	}
	
	@Operation(summary = "Delete post")
	@DeleteMapping(value = "/{postId}")
	public ResponseEntity<?> deletePost(
			@PathVariable(value = "postId", required = true) @Size(max = 36) String postId) throws IOException {

		Post post = postService.findByPostIdAndUserId(postId, userService.getUserId());
		if (post == null) {
			return ResponseEntity.ok().body("Post not found!");
		}
		postService.deletePost(post.getPostId());

		return ResponseEntity.ok().body("Delete post successfull!");
	}

	@Operation(summary = "Get all post of me and friend")
	@GetMapping
	public @ResponseBody ResponseEntity<List<GetPostResDto>> getAllPost( 
			@RequestParam(value = "timeStart", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date timeStart,
			@RequestParam(value = "timeEnd", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date timeEnd,
			@RequestParam(value = "limit-post", required = true) int limitPost,
			@RequestParam(value = "offset-post", required = true) int offsetPost,
			@RequestParam(value = "limit-comment", required = true) int limitComment,
			@RequestParam(value = "offset-comment", required = true) int offsetComment) {
		List<GetPostResDto> response = postService.getAllPost(timeStart, timeEnd, limitPost, offsetPost, limitComment, offsetComment);
		return ResponseEntity.ok().body(response);
	}
	
	@Operation(summary = "Get post of me or friend")
	@GetMapping(value = "/{postId}")
	public @ResponseBody ResponseEntity<GetPostResDto> getPost( 
			@PathVariable(value = "postId", required = true) @Size(max = 36) String postId,
			@RequestParam(value = "limit-comment", required = true) int limitComment,
			@RequestParam(value = "offset-comment", required = true) int offsetComment) {
		GetPostResDto response = postService.getPost(postId, limitComment, offsetComment);
		return ResponseEntity.ok().body(response);
	}

}
