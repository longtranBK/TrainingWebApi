package com.example.demo.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.request.InsertPostReqDto;
import com.example.demo.dto.request.UpdatePostReqDto;
import com.example.demo.dto.response.CommentCustomResDto;
import com.example.demo.dto.response.CommentInfor;
import com.example.demo.dto.response.GetPostResDto;
import com.example.demo.dto.response.PostUpdateResDto;
import com.example.demo.entity.Post;
import com.example.demo.entity.PostImage;
import com.example.demo.repository.CommentLikeRepository;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PostImageRepository;
import com.example.demo.repository.PostLikeRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.security.jwt.AuthEntryPointJwt;
import com.example.demo.service.FileService;
import com.example.demo.service.PostService;
import com.example.demo.service.UserService;

@Service
public class PostServiceImpl implements PostService {
	
	@Value("${upload.path}")
	private String uploadPath;

	private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private PostImageRepository postImageRepository;

	@Autowired
	private PostLikeRepository postLikeRepository;
	
	@Autowired
	private CommentLikeRepository commentLikeRepository;


	@Autowired
	private CommentRepository commentRepository;

	@Override
	public Post findByPostId(String postId) {
		String userId = userService.getUserId();
		return postRepository.findByPostIdOfmeOrFriend(userId, postId);
	}

	@Override
	@Transactional(rollbackOn = { Exception.class, Throwable.class })
	public Post insertPost(InsertPostReqDto request, MultipartFile[] imageList, String userId) {
		String postId = UUID.randomUUID().toString();

		Post post = new Post();
		post.setPostId(postId);
		post.setUserId(userId);
		post.setContent(request.getContent());
		String path = post.getUserId() + "/" + post.getPostId() + "/";
		Post postSave = postRepository.save(post);
		if (imageList == null || (imageList.length == savePostImageList(post.getPostId(), imageList, path).size())) {
			return postSave;
		}
		return null;
	}
	

	@Override
	@Transactional(rollbackOn = { Exception.class, Throwable.class })
	public PostUpdateResDto updatePost(Post post, UpdatePostReqDto request, MultipartFile[] avatarList) {
		PostUpdateResDto res = new PostUpdateResDto();
		post.setContent(request.getContent());
		Post postSave = postRepository.save(post);
		String path = post.getUserId() + "/" + post.getPostId() + "/";

		if (postSave != null && avatarList == null) {
			res.setContent(postSave.getContent());
			return res;
		}
		res.setContent(postSave.getContent());
		List<PostImage> listImage = savePostImageList(post.getPostId(), avatarList, path);
		listImage.forEach(image -> {
			res.getPostImageUrlList().add(image.getImageUrl());
		});
		
		return res;
	}

	@Override
	@Transactional(rollbackOn = { Exception.class, Throwable.class })
	public void deletePost(String postId) throws IOException {
		// Delete table post
		postRepository.updateDelFlg(postId, true);
		
		// Delete url post image
		postImageRepository.deleteImageUrl(postId);
		FileUtils.deleteDirectory(new File(uploadPath + "/" +userService.getUserId() + "/" + postId));
		
		// Delete table comment
		commentRepository.updateDelFlg(postId, true);
	}

	/**
	 * Save all image of post
	 * 
	 * @param postId
	 * @param imageList
	 * @return PostImageList
	 */
	private List<PostImage> savePostImageList(String postId, MultipartFile[] imageList, String path) {
		List<PostImage> postImageList = new ArrayList<>();
		if (imageList.length > 0) {
			postImageRepository.deleteImageUrl(postId);
			Arrays.asList(imageList).stream().forEach(file -> {
				PostImage image = new PostImage();
				try {
					image.setImageUrl(fileService.saveOneFile(file, path));
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
				image.setPostId(postId);
				postImageList.add(image);
			});
		}
		return postImageRepository.saveAll(postImageList);
	}

	@Override
	public Post findByPostIdAndUserId(String postId, String userId) {
		return postRepository.findByPostIdAndUserIdAndDelFlg(postId, userId, false);
	}

	@Override
	public List<GetPostResDto> getAllPost(Date startDate, Date endDate, int limitPost,
			int offsetPost, int limitComment, int offsetComment) {
		List<GetPostResDto> response = new ArrayList<>();
		String userId = userService.getUserId();
		java.sql.Date start = new java.sql.Date(startDate.getTime());
		java.sql.Date end = new java.sql.Date(endDate.getTime());
		List<Post> postList = postRepository.getAllPosts(userId, start, end, limitPost, offsetPost);
		if (postList.size() == 0) {
			return response;
		}
		
		postList.forEach(post -> {
			GetPostResDto postData = new GetPostResDto();
			postData.setPostId(post.getPostId());
			postData.setContent(post.getContent());

			List<String> captureUrlList = postImageRepository.findByPostId(post.getPostId());
			postData.setCaptureUrlList(captureUrlList);

			postData.setCountLikes(postLikeRepository.countTotalLike(post.getPostId()));

			List<CommentCustomResDto> commentListCus = commentRepository.findByPostIdCustom(post.getPostId(), limitComment, offsetComment);
			
			List<CommentInfor> listCommentView = new ArrayList<>();
			commentListCus.forEach(comment -> {
				CommentInfor cmt = new CommentInfor();
				cmt.setCommentId(comment.getCommentId());
				cmt.setContent(comment.getContent());
				cmt.setCreateTs(comment.getCreateTs());
				cmt.setUpdateTs(comment.getUpdateTs());
				cmt.setCountLike(commentLikeRepository.countLike(comment.getCommentId()));
				listCommentView.add(cmt);
			});
			postData.setCommentList(listCommentView);
			response.add(postData);
		});
		return response;
	}

	@Override
	public GetPostResDto getPost(String postId, int limitComment, int offsetComment) {
		Post post = postRepository.findByPostIdOfmeOrFriend(userService.getUserId(), postId);
		if (post == null) {
			return null;
		}
		GetPostResDto postData = new GetPostResDto();
		
		postData.setPostId(post.getPostId());
		postData.setContent(post.getContent());
		List<String> captureUrlList = postImageRepository.findByPostId(post.getPostId());
		postData.setCaptureUrlList(captureUrlList);
		postData.setCountLikes(postLikeRepository.countTotalLike(post.getPostId()));
		List<CommentCustomResDto> commentListCus = commentRepository.findByPostIdCustom(post.getPostId(), limitComment, offsetComment);
		
		List<CommentInfor> listCommentView = new ArrayList<>();
		commentListCus.forEach(comment -> {
			CommentInfor cmt = new CommentInfor();
			cmt.setCommentId(comment.getCommentId());
			cmt.setContent(comment.getContent());
			cmt.setCreateTs(comment.getCreateTs());
			cmt.setUpdateTs(comment.getUpdateTs());
			cmt.setCountLike(commentLikeRepository.countLike(comment.getCommentId()));
			listCommentView.add(cmt);
		});
		postData.setCommentList(listCommentView);
		
		return postData;
	}
	
}
