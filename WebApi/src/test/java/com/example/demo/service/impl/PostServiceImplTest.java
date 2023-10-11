package com.example.demo.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.sql.Timestamp;
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
import com.example.demo.entity.Capture;
import com.example.demo.entity.Like;
import com.example.demo.entity.Post;
import com.example.demo.repository.CaptureRepository;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.LikeRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.service.FileService;

@SpringBootTest
public class PostServiceImplTest {
	@MockBean
	private FileService fileService;

	@MockBean
	private PostRepository postRepository;

	@MockBean
	private CaptureRepository captureRepository;

	@MockBean
	private LikeRepository likeRepository;

	@MockBean
	private CommentRepository commentRepository;

	@Autowired
	private PostServiceImpl postServiceImpl;

	@Test
	void findByPostId_withInput_returnPost() {
		when(postRepository.findByPostId(any())).thenReturn(new Post());
		Post result = postServiceImpl.findByPostId("id");
		assertNotEquals(null, result);
	}

	@Test
	void insertPost_withInput_returnNull() {
		when(postRepository.save(any())).thenReturn(new Post());
		InsertPostReqDto insertPostReqDto = new InsertPostReqDto();
		byte[] content = "test".getBytes();
		MultipartFile[] file = {new MockMultipartFile("name", "file.txt", "text/plain", content)};
		Post result = postServiceImpl.insertPost(insertPostReqDto, file, "userId");
		assertEquals(null, result);
		
	}

	@Test
	void insertPost_withInput_returnPost() {
		when(postRepository.save(any())).thenReturn(new Post());
		when(captureRepository.saveAll(any())).thenReturn(List.of(new Capture()));
		InsertPostReqDto insertPostReqDto = new InsertPostReqDto();
		byte[] content = "test".getBytes();
		MultipartFile[] file = {new MockMultipartFile("name", "file.txt", "text/plain", content)};
		Post result = postServiceImpl.insertPost(insertPostReqDto, file, "userId");
		assertNotEquals(null, result);
		
	}

	@Test
	void getPostCustom_withInput_returnEmpty() {
		when(postRepository.getPostsCustom(anyString(), any(Date.class), any(Date.class), anyInt())).thenReturn(List.of());
		List<GetPostResDto> result = postServiceImpl.getPostCustom("id", new Date(1), new Date(1), 0);
		assertEquals(0, result.size());
	}

	@Test
	void getPostCustom_withInput_returnListGetPostResDto() {
		Post post = new Post();
		post.setContent("content");
		post.setCreateTs(new Timestamp(0));
		post.setPostId("id");
		post.setStatus(0);
		post.setUpdateTs(new Timestamp(0));
		post.setUserId("id");
		when(postRepository.getPostsCustom(anyString(), any(Date.class), any(Date.class), anyInt()))
				.thenReturn(List.of(post));
		List<GetPostResDto> result = postServiceImpl.getPostCustom("id", new Date(1), new Date(1), 0);
		assertEquals(1, result.size());
	}

	@Test
	void updatePost_withInput_returnNull() {
		when(postRepository.save(any())).thenReturn(new Post());
		UpdatePostReqDto updatePostReqDto = new UpdatePostReqDto();
		byte[] content = "test".getBytes();
		MultipartFile[] file = {new MockMultipartFile("name", "file.txt", "text/plain", content)};
		Post result = postServiceImpl.updatePost(new Post(), updatePostReqDto, file);
		assertEquals(null, result);
		
	}

	@Test
	void updatePost_withInput_returnPost() {
		when(postRepository.save(any())).thenReturn(new Post());
		when(captureRepository.saveAll(any())).thenReturn(List.of(new Capture()));
		UpdatePostReqDto updatePostReqDto = new UpdatePostReqDto();
		byte[] content = "test".getBytes();
		MultipartFile[] file = {new MockMultipartFile("name", "file.txt", "text/plain", content)};
		Post result = postServiceImpl.updatePost(new Post(), updatePostReqDto, file);
		assertNotEquals(null, result);
		
	}

	@Test
	void deletePost_withInput_excuteSucess() {
		postServiceImpl.deletePost(new Post());
	}

	@Test
	void getPostTimeLine_withInput_returnEmpty() {
		when(postRepository.getPostTimeline(anyString(), anyInt())).thenReturn(List.of());
		List<GetPostResDto> result = postServiceImpl.getPostTimeLine(0, "id");
		assertEquals(0, result.size());
	}

	@Test
	void getPostTimeLine_withInput_returnListGetPostResDto() {
		Post post = new Post();
		post.setContent("content");
		post.setCreateTs(new Timestamp(0));
		post.setPostId("id");
		post.setStatus(0);
		post.setUpdateTs(new Timestamp(0));
		post.setUserId("id");
		when(postRepository.getPostTimeline(anyString(), anyInt())).thenReturn(List.of(post));
		List<GetPostResDto> result = postServiceImpl.getPostTimeLine(0, "id");
		assertEquals(1, result.size());
	}

	@Test
	void hasLike_withInput_returnTrue() {
		when(likeRepository.hasLike(any(), any())).thenReturn(true);
		boolean result = postServiceImpl.hasLike("id", "id");
		assertEquals(true, result);
	}

	@Test
	void likePost_withInput_returnLike() {
		Like likePost = new Like();
		when(likeRepository.save(any())).thenReturn(likePost);
		Like result = postServiceImpl.likePost("id", "id");
		assertNotEquals(null, result);
	}

	@Test
	void dislikePost_withInput_excuteSucess() {
		postServiceImpl.dislikePost("id", "id");
	}

	@Test
	void findByPostIdAndUserId_withInput_returnPost() {
		when(postRepository.findByPostIdAndUserId(any(), any())).thenReturn(new Post());
		Post result =postServiceImpl.findByPostIdAndUserId("id", "id");
		assertNotEquals(null, result);
	}
}
