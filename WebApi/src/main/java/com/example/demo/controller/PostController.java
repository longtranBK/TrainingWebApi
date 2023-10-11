package com.example.demo.controller;

import java.sql.Date;
import java.text.ParseException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
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
@RestController
@RequestMapping("api/posts")
public class PostController {

	@Autowired
	private UserService userService;

	@Autowired
	private PostService postService;

	@Operation(summary = "Insert new post")
	@PostMapping(value = { "" })
	@Secured(Constants.ROLE_USER_NAME)
	public ResponseEntity<?> insertPost(@Valid @RequestPart("request") InsertPostReqDto request,
			@RequestPart("files") MultipartFile[] avatarFiles) {

		if (postService.insertPost(request, avatarFiles, userService.getUserId()) != null) {
			return ResponseEntity.ok().body("Insert post successful!");
		} else {
			return ResponseEntity.ok().body("Insert post error!");
		}

	}

	@Operation(summary = "Search post")
	@GetMapping(value = "")
	@Secured(Constants.ROLE_USER_NAME)
	public @ResponseBody ResponseEntity<List<GetPostResDto>> getPost(@RequestParam("userId") String userId,
			@RequestParam("timeStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) java.util.Date timeStart,
			@RequestParam("timeEnd") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) java.util.Date timeEnd,
			@RequestParam("numbersPost") int numbersPost) {
		Date start = new java.sql.Date(timeStart.getTime());
		Date end = new java.sql.Date(timeEnd.getTime());

		List<GetPostResDto> response = postService.getPostCustom(userId, start, end, numbersPost);
		return ResponseEntity.ok().body(response);
	}

	@Operation(summary = "Update post")
	@PostMapping(value = "/{postId}")
	@Secured(Constants.ROLE_USER_NAME)
	public ResponseEntity<?> updatePost(@PathVariable(value = "postId") String postId,
			@Valid @RequestPart("request") UpdatePostReqDto request, @RequestPart("files") MultipartFile[] avatarFiles)
			throws ParseException {

		Post post = postService.findByPostIdAndUserId(postId, userService.getUserId());
		if (post == null) {
			return ResponseEntity.notFound().build();
		}

		if(postService.updatePost(post, request, avatarFiles) != null) {
			return ResponseEntity.ok().body("Update post successfull!");
		} else {
			return ResponseEntity.ok().body("Update post error!");
		}
	}

	@Operation(summary = "Delete post")
	@DeleteMapping(value = { "/{postId}" })
	@Secured(Constants.ROLE_USER_NAME)
	public ResponseEntity<?> deletePost(@PathVariable(value = "postId") String postId) {

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
			@RequestParam("numbers-post") int numbersPost) {

		List<GetPostResDto> response = postService.getPostTimeLine(numbersPost, userService.getUserId());
		return ResponseEntity.ok().body(response);
	}

	@Operation(summary = "Like post")
	@PostMapping(value = { "/like" })
	@Secured(Constants.ROLE_USER_NAME)
	public ResponseEntity<?> likePost(@Valid @RequestBody LikePostReqDto request) {

		String userId = userService.getUserId();
		if (postService.hasLike(userId, request.getPostId())) {
			return ResponseEntity.ok().body("User had liked!");
		}
		if( postService.likePost(userId, request.getPostId())!= null) {
			return ResponseEntity.ok().body("Like post successful!");
		} else {
			return ResponseEntity.ok().body("Like post error!");
		}
	}

	@Operation(summary = "Dislike post")
	@DeleteMapping(value = { "/dislike" })
	@Secured(Constants.ROLE_USER_NAME)
	public ResponseEntity<?> dislikePost(@Valid @RequestBody LikePostReqDto request) {

		String userId = userService.getUserId();
		if (!postService.hasLike(userId, request.getPostId())) {
			return ResponseEntity.ok().body("User had not like!");
		}
		postService.dislikePost(userId, request.getPostId());

		return ResponseEntity.ok().body("Unlike successful!");
	}

}
