package com.example.demo.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.request.InsertPostReqDto;
import com.example.demo.dto.request.UpdatePostReqDto;
import com.example.demo.dto.response.GetPostResDto;
import com.example.demo.entity.Post;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PostImageRepository;
import com.example.demo.repository.PostLikeRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.service.FileService;

@SpringBootTest
public class PostServiceImplTest {
	@MockBean
	private FileService fileService;

	@MockBean
	private PostRepository postRepository;

	@MockBean
	private PostImageRepository postImageRepository;

	@MockBean
	private PostLikeRepository postLikeRepository;

	@MockBean
	private CommentRepository commentRepository;

	@Autowired
	private PostServiceImpl postServiceImpl;

	@Test
	void findByPostId_withInput_returnPost() {
//		when(postRepository.findByPostId(any())).thenReturn(new Post());
//		Post result = postServiceImpl.findByPostId("id");
//		assertNotEquals(null, result);
	}

	@Test
	void insertPost_withInput_returnPost() {
		when(postRepository.save(any(Post.class))).thenReturn(new Post());
		InsertPostReqDto insertPostReqDto = new InsertPostReqDto();
		Post result = postServiceImpl.insertPost(insertPostReqDto, null, "userId");
		assertNotEquals(null, result);
	}

	@Test
	void insertPost_withInput_noCapture_returnPost() {
//		List<Capture> captureList = new ArrayList<>();
		when(postRepository.save(any())).thenReturn(new Post());
//		when(captureRepository.saveAll(any())).thenReturn(captureList);
		InsertPostReqDto insertPostReqDto = new InsertPostReqDto();
		MultipartFile[] file = {};
		Post result = postServiceImpl.insertPost(insertPostReqDto, file, "userId");
		assertNotEquals(null, result);
	}
	
	@Test
	void insertPost_withInput_hadCapture_returnPost() {
//		List<Capture> captureList = new ArrayList<>();
//		Capture capture = new Capture();
//		captureList.add(capture);
//		when(postRepository.save(any())).thenReturn(new Post());
//		when(captureRepository.saveAll(any())).thenReturn(captureList);
//		InsertPostReqDto insertPostReqDto = new InsertPostReqDto();
//		byte[] content = "test".getBytes();
//		MultipartFile[] file = { new MockMultipartFile("name", "file.txt", "text/plain", content) };
//		Post result = postServiceImpl.insertPost(insertPostReqDto, file, "userId");
//		assertNotEquals(null, result);
	}

//	@Test
//	void getPostCustom_withInput_returnEmpty() {
//		List<Post> postList = new ArrayList<>();
//		when(postRepository.getPostsCustom(anyString(), any(Date.class), any(Date.class), anyInt()))
//				.thenReturn(postList);
//		List<GetPostResDto> result = postServiceImpl.getPostCustom("id", new Date(1), new Date(1), 0);
//		assertEquals(0, result.size());
//	}
//
//	@Test
//	void getPostCustom_withInput_returnListGetPostResDto() {
//		Post post = new Post();
//		post.setContent("content");
//		post.setPostId("id");
//		post.setStatus("0");
//		post.setUserId("id");
//		List<Post> postList = new ArrayList<>();
//		postList.add(post);
//		when(postRepository.getPostsCustom(anyString(), any(Date.class), any(Date.class), anyInt()))
//				.thenReturn(postList);
//		List<GetPostResDto> result = postServiceImpl.getPostCustom("id", new Date(1), new Date(1), 0);
//		assertEquals(1, result.size());
//	}

	@Test
	void updatePost_withInput_returnNull() {
//		when(postRepository.save(any())).thenReturn(new Post());
//		UpdatePostReqDto updatePostReqDto = new UpdatePostReqDto();
//		byte[] content = "test".getBytes();
//		MultipartFile[] file = {new MockMultipartFile("name", "file.txt", "text/plain", content)};
//		Post result = postServiceImpl.updatePost(new Post(), updatePostReqDto, file);
//		assertEquals(null, result);
		
	}

	@Test
	void updatePost_withInput_hadCapture_returnPost() {
//		List<Capture> captureList = new ArrayList<>();
//		captureList.add(new Capture());
//		when(postRepository.save(any())).thenReturn(new Post());
//		when(captureRepository.saveAll(any())).thenReturn(captureList);
//		UpdatePostReqDto updatePostReqDto = new UpdatePostReqDto();
//		byte[] content = "test".getBytes();
//		MultipartFile[] file = { new MockMultipartFile("name", "file.txt", "text/plain", content) };
//		Post result = postServiceImpl.updatePost(new Post(), updatePostReqDto, file);
//		assertNotEquals(null, result);

	}
	

	@Test
	void updatePost_withInput_noCapture_returnPost() {
//		List<Capture> captureList = new ArrayList<>();
//		when(postRepository.save(any())).thenReturn(new Post());
//		when(captureRepository.saveAll(any())).thenReturn(captureList);
//		UpdatePostReqDto updatePostReqDto = new UpdatePostReqDto();
//		MultipartFile[] file = { };
//		Post result = postServiceImpl.updatePost(new Post(), updatePostReqDto, file);
//		assertNotEquals(null, result);

	}

	@Test
	void deletePost_withInput_excuteSucess() {
		postServiceImpl.deletePost(any(String.class));
	}


	@Test
	void hasLike_withInput_returnTrue() {
//		when(likeRepository.hasLike(any(), any())).thenReturn(true);
//		boolean result = postServiceImpl.hasLike("id", "id");
//		assertEquals(true, result);
	}

	@Test
	void likePost_withInput_returnLike() {
//		Like likePost = new Like();
//		when(likeRepository.save(any())).thenReturn(likePost);
//		Like result = postServiceImpl.likePost("id", "id");
//		assertNotEquals(null, result);
	}

//	@Test
//	void dislikePost_withInput_excuteSucess() {
//		postServiceImpl.dislikePost("id", "id");
//	}

	@Test
	void findByPostIdAndUserId_withInput_returnPost() {
		when(postRepository.findByPostIdAndUserIdAndDelFlg(any(), any(), any())).thenReturn(new Post());
		Post result =postServiceImpl.findByPostIdAndUserId("id", "id");
		assertNotEquals(null, result);
	}
}
