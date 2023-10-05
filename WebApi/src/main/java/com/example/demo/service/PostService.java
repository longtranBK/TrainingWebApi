package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Post;
import com.example.demo.repository.PostRepository;
import com.example.demo.service.impl.PostServiceInterface;

@Service
public class PostService implements PostServiceInterface{

	@Autowired
	private PostRepository postRepository;
	
	@Override
	public Post findByPostId(String postId) {
		return postRepository.findByPostId(postId);
	}

}
