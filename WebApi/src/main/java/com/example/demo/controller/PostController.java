package com.example.demo.controller;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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
import com.example.demo.dto.request.UpdatePostReqDto;
import com.example.demo.dto.response.CommentCustomInterface;
import com.example.demo.dto.response.GetPostResDto;
import com.example.demo.entity.Capture;
import com.example.demo.entity.Post;
import com.example.demo.entity.User;
import com.example.demo.repository.CaptureRepository;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.LikeRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;

@RestController
@RequestMapping("api/posts")
public class PostController {

	@Autowired
	private CaptureRepository captureRepository;

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private LikeRepository likeRepository;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private UserRepository usersRepository;

	@PostMapping(value = { "" })
	@Transactional(rollbackOn = { Exception.class, Throwable.class })
	public ResponseEntity<?> insertPost(@Valid @RequestBody InsertPostReqDto request) {

		User user = usersRepository.findUserById(request.getUserId());

		if (user == null) {
			return ResponseEntity.ok().body("User id not found!");
		}

		String postId = UUID.randomUUID().toString();
		Timestamp upadteTs = new java.sql.Timestamp(System.currentTimeMillis());

		Post post = new Post();
		post.setPostId(postId);
		post.setUserId(request.getUserId());
		post.setContent(request.getContent());
		post.setStatus(request.getStatus());
		post.setCreateTs(upadteTs);
		post.setUpdateTs(upadteTs);
		postRepository.save(post);

		if (request.getCapturesUrl().size() > 0) {
			List<Capture> captureList = new ArrayList<>();

			for (int i = 0; i < request.getCapturesUrl().size(); i++) {
				String captureId = UUID.randomUUID().toString();
				Capture capture = new Capture();
				capture.setCaptureId(captureId);
				capture.setPostId(postId);
				captureList.add(capture);
			}
			captureRepository.saveAll(captureList);
		}

		return ResponseEntity.ok().body("Insert post successful!");

	}

	@GetMapping(value = "")
	public @ResponseBody ResponseEntity<List<GetPostResDto>> getPost(@RequestParam("userId") String userId,
			@RequestParam("timeStart") @DateTimeFormat(pattern = "yyyy-MM-dd") Date timeStart,
			@RequestParam("timeEnd") @DateTimeFormat(pattern = "yyyy-MM-dd") Date timeEnd,
			@RequestParam("numbersPost") int numbersPost) {
		List<GetPostResDto> response = new ArrayList<>();

		Date start = new java.sql.Date(timeStart.getTime());
		Date end = new java.sql.Date(timeEnd.getTime());

		List<Post> postList = postRepository.getPostsCustom(userId, start, end, numbersPost);
		if (postList.size() == 0) {
			return ResponseEntity.ok().body(response);
		}

		for (int i = 0; i < postList.size(); i++) {
			GetPostResDto post = new GetPostResDto();
			post.setPostId(postList.get(i).getPostId());
			post.setContent(postList.get(i).getContent());
			post.setStatus(postList.get(i).getStatus());
			post.setCreateTs(postList.get(i).getCreateTs());
			post.setUpdateTs(postList.get(i).getUpdateTs());

			List<String> captureUrlList = captureRepository.findByPostId(postList.get(i).getPostId());
			post.setCaptureUrlList(captureUrlList);

			List<String> userIdLikeList = likeRepository.findByPostId(postList.get(i).getPostId());
			post.setUserIdLikeList(userIdLikeList);

			List<CommentCustomInterface> commentList = commentRepository
					.findByPostIdCustom(postList.get(i).getPostId());
			post.setCommentList(commentList);

			response.add(post);
		}
		return ResponseEntity.ok().body(response);
	}

	@PutMapping(value = { "/{postId}" })
	@Transactional(rollbackOn = { Exception.class, Throwable.class })
	public ResponseEntity<?> updatePost(@PathVariable(value = "postId") String postId,
			@Valid @RequestBody UpdatePostReqDto request) throws ParseException {

		Timestamp upadteTs = new java.sql.Timestamp(System.currentTimeMillis());

		Post post = postRepository.findByPostId(postId);

		if (post == null) {
			return ResponseEntity.notFound().build();
		}

		post.setContent(request.getContent());
		post.setStatus(request.getStatus());
		post.setUpdateTs(upadteTs);
		postRepository.save(post);

		if (request.getCaptureUrlList().size() > 0) {
			captureRepository.deleteByPostId(postId);

			for (int i = 0; i < request.getCaptureUrlList().size(); i++) {
				String captureId = UUID.randomUUID().toString();
				Capture capture = new Capture();
				capture.setCaptureId(captureId);
				capture.setPostId(postId);
				capture.setCaptureUrl(request.getCaptureUrlList().get(i));
				captureRepository.save(capture);
			}
		}
		String msg = "Update post successfull!";
		return ResponseEntity.ok().body(msg);
	}

	@DeleteMapping(value = { "/{postId}" })
	@Transactional(rollbackOn = { Exception.class, Throwable.class })
	public ResponseEntity<?> deletePost(@PathVariable(value = "postId") String postId) {

		Post post = postRepository.findByPostId(postId);

		if (post == null) {
			return ResponseEntity.notFound().build();
		}

		// Delete table likes
		likeRepository.deleteByPostId(postId);

		// Delete table comment
		commentRepository.deleteByPostId(postId);

		// Delete table captures
		captureRepository.deleteByPostId(postId);

		// Delete table post
		postRepository.delete(post);

		String msg = "Delete post successfull!";
		return ResponseEntity.ok().body(msg);
	}

	@GetMapping(value = "/timeline")
	public @ResponseBody ResponseEntity<List<GetPostResDto>> getPostTimeLine(
			@RequestParam("numbers-post") int numbersPost) {

		SecurityContext securityContext = SecurityContextHolder.getContext();
		String username = securityContext.getAuthentication().getName();
		User user = usersRepository.findByUsername(username);
		
		List<Post> postList = postRepository.getPostTimeline(user.getUserId(), numbersPost);
		if (postList.size() == 0) {
			return ResponseEntity.notFound().build();
		}
		
		List<GetPostResDto> response = new ArrayList<>();
		for (int i = 0; i < postList.size(); i++) {
			GetPostResDto post = new GetPostResDto();
			post.setPostId(postList.get(i).getPostId());
			post.setContent(postList.get(i).getContent());
			post.setStatus(postList.get(i).getStatus());
			post.setCreateTs(postList.get(i).getCreateTs());
			post.setUpdateTs(postList.get(i).getUpdateTs());

			List<String> captureUrlList = captureRepository.findByPostId(postList.get(i).getPostId());
			post.setCaptureUrlList(captureUrlList);

			List<String> userIdLikeList = likeRepository.findByPostId(postList.get(i).getPostId());
			post.setUserIdLikeList(userIdLikeList);

			List<CommentCustomInterface> commentList = commentRepository
					.findByPostIdCustom(postList.get(i).getPostId());
			post.setCommentList(commentList);

			response.add(post);
		}
		return ResponseEntity.ok().body(response);
	}
}
