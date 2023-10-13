package com.example.demo.controller;

import java.sql.Date;
import java.text.ParseException;
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
import com.example.demo.entity.Post;
import com.example.demo.service.PostService;
import com.example.demo.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Post", description = "API thao tác với post")
@Validated
@RestController
@RequestMapping("api/posts")
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

	@Operation(summary = "Search post")
	@GetMapping
	@Secured(Constants.ROLE_USER_NAME)
	public @ResponseBody ResponseEntity<List<GetPostResDto>> getPost( 
			@RequestParam(value = "userId", required = true) @NotBlank @Size(max = 36) String userId,
			@RequestParam(value = "timeStart", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) java.util.Date timeStart,
			@RequestParam(value = "timeEnd", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) java.util.Date timeEnd,
			@RequestParam(value = "numbersPost", required = true) int numbersPost) {
		Date start = new java.sql.Date(timeStart.getTime());
		Date end = new java.sql.Date(timeEnd.getTime());

		List<GetPostResDto> response = postService.getPostCustom(userId, start, end, numbersPost);
		return ResponseEntity.ok().body(response);
	}

	@Operation(summary = "Update post")
	@PostMapping(value = "/{postId}", consumes = "multipart/form-data")
	@Secured(Constants.ROLE_USER_NAME)
	public ResponseEntity<?> updatePost(
			@PathVariable(value = "postId", required = true) @Size(max = 36) String postId,
			@Valid @RequestPart(value = "request", required = true) UpdatePostReqDto request, 
			@RequestPart(value = "files", required = false) MultipartFile[] avatarFiles)
			throws ParseException {

		Post post = postService.findByPostIdAndUserId(postId, userService.getUserId());
		if (post == null) {
			return ResponseEntity.notFound().build();
		}

		if(postService.updatePost(post, request, avatarFiles) != null) {
			return ResponseEntity.ok().body("Update post successfull!");
		} else {
			return ResponseEntity.internalServerError().body("Update post error!");
		}
	}

	@Operation(summary = "Delete post")
	@DeleteMapping(value = { "/{postId}" })
	@Secured(Constants.ROLE_USER_NAME)
	public ResponseEntity<?> deletePost(
			@PathVariable(value = "postId", required = true) @Size(max = 36) String postId) {

		Post post = postService.findByPostIdAndUserId(postId, userService.getUserId());
		if (post == null) {
			return ResponseEntity.notFound().build();
		}
		postService.deletePost(post);

		return ResponseEntity.ok().body("Delete post successfull!");
	}

	@Operation(summary = "Get post in timeline after login")
	@GetMapping(value = "/timeline")
	@Secured(Constants.ROLE_USER_NAME)
	public @ResponseBody ResponseEntity<List<GetPostResDto>> getPostTimeLine(
			@RequestParam(value = "numbers-post", required = true) int numbersPost) {

		List<GetPostResDto> response = postService.getPostTimeline(userService.getUserId(), numbersPost);
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
		if( postService.likePost(userId, request.getPostId())!= null) {
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
