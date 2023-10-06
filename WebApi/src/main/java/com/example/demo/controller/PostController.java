package com.example.demo.controller;

import java.sql.Date;
import java.text.ParseException;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
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

import com.example.demo.dto.request.InsertPostReqDto;
import com.example.demo.dto.request.LikePostReqDto;
import com.example.demo.dto.request.UpdatePostReqDto;
import com.example.demo.dto.response.GetPostResDto;
import com.example.demo.entity.Post;
import com.example.demo.entity.User;
import com.example.demo.service.PostService;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("api/posts")
public class PostController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PostService postService;
	
	@PostMapping(value = { "" })
	public ResponseEntity<?> insertPost(@Valid @RequestBody InsertPostReqDto request) {

		User user = userService.getByUserId(request.getUserId());
		if (user == null) {
			return ResponseEntity.ok().body("User id not found!");
		}
		
		postService.insertPost(request);
		return ResponseEntity.ok().body("Insert post successful!");
	}

	@GetMapping(value = "")
	public @ResponseBody ResponseEntity<List<GetPostResDto>> getPost(
			@RequestParam("userId") String userId,
			@RequestParam("timeStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) java.util.Date timeStart,
			@RequestParam("timeEnd") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) java.util.Date timeEnd,
			@RequestParam("numbersPost") int numbersPost) {
		Date start = new java.sql.Date(timeStart.getTime());
		Date end = new java.sql.Date(timeEnd.getTime());
		
		List<GetPostResDto> response = postService.getPostCustom(userId, start, end, numbersPost);
		return ResponseEntity.ok().body(response);
	}

	@PutMapping(value = { "/{postId}" })
	@Transactional(rollbackOn = { Exception.class, Throwable.class })
	public ResponseEntity<?> updatePost(@PathVariable(value = "postId") String postId,
			@Valid @RequestBody UpdatePostReqDto request) throws ParseException {

		Post post = postService.findByPostId(postId);

		if (post == null) {
			return ResponseEntity.notFound().build();
		}

		postService.updatePost(post, request);
		return ResponseEntity.ok().body("Update post successfull!");
	}

	@DeleteMapping(value = { "/{postId}" })
	public ResponseEntity<?> deletePost(@PathVariable(value = "postId") String postId) {

		Post post = postService.findByPostId(postId);
		if (post == null) {
			return ResponseEntity.notFound().build();
		}
		postService.deletePost(post);

		return ResponseEntity.ok().body("Delete post successfull!");
	}

	@GetMapping(value = "/timeline")
	public @ResponseBody ResponseEntity<List<GetPostResDto>> getPostTimeLine(
			@RequestParam("numbers-post") int numbersPost) {
	
		List<GetPostResDto> response = postService.getPostTimeLine(numbersPost);
		if (response.size() == 0) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok().body(response);
	}
	
	@PostMapping(value = { "/like" })
	public ResponseEntity<?> likePost(@Valid @RequestBody LikePostReqDto request) {

		if(postService.hasLike(request.getUserId(), request.getPostId())) {
			return ResponseEntity.ok().body("User had liked!");
		}
		postService.likePost(request.getUserId(), request.getPostId());
		
		return ResponseEntity.ok().body("Like post successful!");
	}
	
	@DeleteMapping(value = { "/dislike" })
	public ResponseEntity<?> dislikePost(@Valid @RequestBody LikePostReqDto request) {

		if(!postService.hasLike(request.getUserId(), request.getPostId())) {
			return ResponseEntity.ok().body("User had not like!");
		}
		postService.dislikePost(request.getUserId(), request.getPostId());
		
		return ResponseEntity.ok().body("Unlike successful!");
	}
}
