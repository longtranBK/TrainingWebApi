package com.example.demo.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.InsertPostReqDto;
import com.example.demo.dto.InsertPostResDto;
import com.example.demo.entity.Capture;
import com.example.demo.entity.Post;
import com.example.demo.entity.User;
import com.example.demo.repository.CaptureRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;

@RestController
@RequestMapping("api")
public class PostController {

	@Autowired
	private CaptureRepository captureRepository;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private UserRepository usersRepository;

	@PostMapping(value = { "/insertPost" })
	@Transactional(rollbackOn = { Exception.class, Throwable.class })
	public ResponseEntity<InsertPostResDto> signup(@Valid @RequestBody InsertPostReqDto request) {
		InsertPostResDto response = new InsertPostResDto();

		User user = usersRepository.findUserById(request.getUserId());

		if (user == null) {
			throw new UsernameNotFoundException("User not found!");
		}

		String postId = UUID.randomUUID().toString();
		Timestamp upadteTs = new java.sql.Timestamp(System.currentTimeMillis());

		Post post = new Post();
		post.setPostId(postId);
		post.setUserId(request.getUserId());
		post.setContent(request.getContent());
		post.setStatus(request.getStatus());
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

		response.setMsg("Insert post successful!");
		return ResponseEntity.ok().body(response);

	}
}
