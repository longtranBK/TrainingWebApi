package com.example.demo.service.impl;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.request.InsertPostReqDto;
import com.example.demo.dto.request.UpdatePostReqDto;
import com.example.demo.dto.response.CommentCustomInterface;
import com.example.demo.dto.response.GetPostResDto;
import com.example.demo.entity.Capture;
import com.example.demo.entity.Like;
import com.example.demo.entity.Post;
import com.example.demo.entity.User;
import com.example.demo.repository.CaptureRepository;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.LikeRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.PostService;

@Service
public class PostServiceImpl implements PostService{

	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private CaptureRepository captureRepository;
	
	@Autowired
	private LikeRepository likeRepository;
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public Post findByPostId(String postId) {
		return postRepository.findByPostId(postId);
	}

	@Override
	@Transactional(rollbackOn = { Exception.class, Throwable.class })
	public void insertPost(InsertPostReqDto request) {
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
				Capture capture = new Capture();
				capture.setPostId(postId);
				captureList.add(capture);
			}
			captureRepository.saveAll(captureList);
		}
		
	}

	@Override
	public List<GetPostResDto> getPostCustom(String userId, Date startDate, Date endDate, int numbersPost) {
		List<GetPostResDto> response = new ArrayList<>();
		List<Post> postList = postRepository.getPostsCustom(userId, startDate, endDate, numbersPost);
		if (postList.size() == 0) {
			return response;
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
		return response;
	}

	@Override
	public void updatePost(Post post, UpdatePostReqDto request) {
		
		Timestamp upadteTs = new java.sql.Timestamp(System.currentTimeMillis());
		post.setContent(request.getContent());
		post.setStatus(request.getStatus());
		post.setUpdateTs(upadteTs);
		postRepository.save(post);

		if (request.getCaptureUrlList().size() > 0) {
			captureRepository.deleteByPostId(post.getPostId());

			for (int i = 0; i < request.getCaptureUrlList().size(); i++) {
				Capture capture = new Capture();
				capture.setPostId(post.getPostId());
				capture.setCaptureUrl(request.getCaptureUrlList().get(i));
				captureRepository.save(capture);
			}
		}
	}

	@Override
	@Transactional(rollbackOn = { Exception.class, Throwable.class })
	public void deletePost(Post post) {
		String postId = post.getPostId();
		
		// Delete table likes
		likeRepository.deleteByPostId(postId);

		// Delete table comment
		commentRepository.deleteByPostId(postId);

		// Delete table captures
		captureRepository.deleteByPostId(postId);

		// Delete table post
		postRepository.delete(post);
	}

	@Override
	public List<GetPostResDto> getPostTimeLine(int numbersPost) {
		List<GetPostResDto> response = new ArrayList<>();
		
		SecurityContext securityContext = SecurityContextHolder.getContext();
		String username = securityContext.getAuthentication().getName();
		User user = userRepository.findByUsername(username);
		
		List<Post> postList = postRepository.getPostTimeline(user.getUserId(), numbersPost);
		if (postList.size() == 0) {
			return response;
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
		
		return response;
	}

	@Override
	public boolean hasLike(String userId, String postId) {
		return likeRepository.hasLike(userId, postId);
	}

	@Override
	public void likePost(String userId, String postId) {
		Like likePost = new Like();
		likePost.setUserId(userId);
		likePost.setPostId(postId);
		likeRepository.save(likePost);
	}

	@Override
	public void dislikePost(String userId, String postId) {
		// TODO Auto-generated method stub
		
	}

}