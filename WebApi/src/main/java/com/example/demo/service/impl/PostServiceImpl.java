package com.example.demo.service.impl;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.request.InsertPostReqDto;
import com.example.demo.dto.request.UpdatePostReqDto;
import com.example.demo.dto.response.CommentCustomResDto;
import com.example.demo.dto.response.GetPostResDto;
import com.example.demo.entity.Capture;
import com.example.demo.entity.Like;
import com.example.demo.entity.Post;
import com.example.demo.repository.CaptureRepository;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.LikeRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.service.FileService;
import com.example.demo.service.PostService;

@Service
public class PostServiceImpl implements PostService {

	@Autowired
	private FileService fileService;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private CaptureRepository captureRepository;

	@Autowired
	private LikeRepository likeRepository;

	@Autowired
	private CommentRepository commentRepository;

	@Override
	public Post findByPostId(String postId) {
		return postRepository.findByPostId(postId);
	}

	@Override
	@Transactional(rollbackOn = { Exception.class, Throwable.class })
	public Post insertPost(InsertPostReqDto request, MultipartFile[] avatarList, String userId) {
		String postId = UUID.randomUUID().toString();
		Timestamp upadteTs = new java.sql.Timestamp(System.currentTimeMillis());

		Post post = new Post();
		post.setPostId(postId);
		post.setUserId(userId);
		post.setContent(request.getContent());
		post.setStatus(request.getStatus());
		post.setCreateTs(upadteTs);
		post.setUpdateTs(upadteTs);
		String path = post.getUserId() + "/" + post.getPostId() + "/";
		Post postSave = postRepository.save(post);
		if (avatarList == null || (avatarList.length == saveCapturesList(post.getPostId(), avatarList, path).size())) {
			return postSave;
		}
		return null;
		
	}

	@Override
	public List<GetPostResDto> getPostCustom(String userId, Date startDate, Date endDate, int numbersPost) {
		List<GetPostResDto> response = new ArrayList<>();
		List<Post> postList = postRepository.getPostsCustom(userId, startDate, endDate, numbersPost);
		if (postList.size() == 0) {
			return response;
		}
		return getData(postList);
	}

	@Override
	@Transactional(rollbackOn = { Exception.class, Throwable.class })
	public Post updatePost(Post post, UpdatePostReqDto request, MultipartFile[] avatarList) {

		Timestamp upadteTs = new java.sql.Timestamp(System.currentTimeMillis());
		post.setContent(request.getContent());
		post.setStatus(request.getStatus());
		post.setUpdateTs(upadteTs);
		Post postSave = postRepository.save(post);
		String path = post.getUserId() + "/" + post.getPostId() + "/";

		if (avatarList == null || (avatarList.length == saveCapturesList(post.getPostId(), avatarList, path).size())) {
			return postSave;
		}
		return null;
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
	public List<GetPostResDto> getPostTimeline(String userId, int numbersPost) {
		List<GetPostResDto> response = new ArrayList<>();

		List<Post> postList = postRepository.getPostTimeline(userId, numbersPost);
		if (postList.size() == 0) {
			return response;
		}

		return getData(postList);
	}

	/**
	 * 
	 * @param postList
	 * @return
	 */
	private List<GetPostResDto> getData(List<Post> postList) {
		List<GetPostResDto> response = new ArrayList<>();
		postList.forEach(post -> {
			GetPostResDto postInser = new GetPostResDto();
			postInser.setPostId(post.getPostId());
			postInser.setContent(post.getContent());
			postInser.setStatus(post.getStatus());
			postInser.setCreateTs(post.getCreateTs());
			postInser.setUpdateTs(post.getUpdateTs());

			List<String> captureUrlList = captureRepository.findByPostId(post.getPostId());
			postInser.setCaptureUrlList(captureUrlList);

			List<String> userIdLikeList = likeRepository.findByPostId(post.getPostId());
			postInser.setUserIdLikeList(userIdLikeList);

			List<CommentCustomResDto> commentList = commentRepository.findByPostIdCustom(post.getPostId());
			postInser.setCommentList(commentList);

			response.add(postInser);
		});

		return response;
	}

	@Override
	public boolean hasLike(String userId, String postId) {
		return likeRepository.hasLike(userId, postId);
	}

	@Override
	public Like likePost(String userId, String postId) {
		Like likePost = new Like();
		likePost.setUserId(userId);
		likePost.setPostId(postId);
		return likeRepository.save(likePost);
	}

	@Override
	public void dislikePost(String userId, String postId) {
		likeRepository.deleteByPostIdAndUserId(postId, userId);
	}

	/**
	 * 
	 * @param postId
	 * @param avatarList
	 * @return
	 */
	private List<Capture> saveCapturesList(String postId, MultipartFile[] avatarList, String path) {
		List<Capture> captureList = new ArrayList<>();
		if (avatarList.length > 0) {
			captureRepository.deleteByPostId(postId);
			Arrays.asList(avatarList).stream().forEach(file -> {
				Capture capture = new Capture();
				capture.setCaptureUrl(fileService.save(file, path));
				capture.setPostId(postId);
				captureList.add(capture);
			});
		}
		return captureRepository.saveAll(captureList);
	}

	@Override
	public Post findByPostIdAndUserId(String postId, String userId) {
		return postRepository.findByPostIdAndUserId(postId, userId);
	}

}
