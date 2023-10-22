package com.example.demo.controller;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.constant.Constants;
import com.example.demo.dto.request.InsertPostReqDto;
import com.example.demo.dto.request.LikePostReqDto;
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
	@Secured(Constants.ROLE_USER_NAME)
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
	@Secured(Constants.ROLE_USER_NAME)
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
	@Secured(Constants.ROLE_USER_NAME)
	public ResponseEntity<?> deletePost(
			@PathVariable(value = "postId", required = true) @Size(max = 36) String postId) {

		Post post = postService.findByPostIdAndUserId(postId, userService.getUserId());
		if (post == null) {
			return ResponseEntity.ok().body("Post not found!");
		}
		postService.deletePost(post.getPostId());

		return ResponseEntity.ok().body("Delete post successfull!");
	}

	@Operation(summary = "Get all post of me and friend")
	@GetMapping
	@Secured(Constants.ROLE_USER_NAME)
	public @ResponseBody ResponseEntity<List<GetPostResDto>> getPost( 
			@RequestParam(value = "timeStart", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date timeStart,
			@RequestParam(value = "timeEnd", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date timeEnd,
			@RequestParam(value = "limit-post", required = true) int limitPost,
			@RequestParam(value = "offset-post", required = true) int offsetPost,
			@RequestParam(value = "limit-comment", required = true) int limitComment,
			@RequestParam(value = "offset-comment", required = true) int offsetComment) {
		List<GetPostResDto> response = postService.getAllPost(timeStart, timeEnd, limitPost, offsetPost, limitComment, offsetComment);
		return ResponseEntity.ok().body(response);
	}

	@Operation(summary = "Like post")
	@PostMapping(value = { "/like" })
	@Secured(Constants.ROLE_USER_NAME)
	public ResponseEntity<?> likePost(
			@Valid @RequestBody(required = true) LikePostReqDto request) {

		String userId = userService.getUserId();
		if (postService.hasLike(userId, request.getPostId())) {
			return ResponseEntity.ok().body("User had liked!");
		}
		if(postService.likePost(userId, request.getPostId())!= null) {
			return ResponseEntity.ok().body("Like post successful!");
		} else {
			return ResponseEntity.internalServerError().body("Like post error!");
		}
	}

	@Operation(summary = "Dislike post")
	@DeleteMapping(value = { "/dislike" })
	@Secured(Constants.ROLE_USER_NAME)
	public ResponseEntity<?> dislikePost(
			@Valid @RequestBody(required = true) LikePostReqDto request) {

		String userId = userService.getUserId();
		if (!postService.hasLike(userId, request.getPostId())) {
			return ResponseEntity.ok().body("User had not like!");
		}
		postService.dislikePost(userId, request.getPostId());

		return ResponseEntity.ok().body("Unlike successful!");
	}

}
