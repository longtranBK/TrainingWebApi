package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.example.demo.constant.Constants;
import com.example.demo.dto.request.InsertPostReqDto;
import com.example.demo.dto.request.LikePostReqDto;
import com.example.demo.dto.request.UpdatePostReqDto;
import com.example.demo.dto.response.GetPostResDto;
import com.example.demo.entity.Like;
import com.example.demo.entity.Post;
import com.example.demo.service.FileService;
import com.example.demo.service.PostService;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
public class PostControllerTests {

	private String uri = "/api/posts";

	private MockMvc mockMvc;

	@Autowired
	WebApplicationContext webApplicationContext;

	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private UserService userService;

	@MockBean
	private PostService postService;

	@MockBean
	private FileService fileService;
	
	@MockBean
	private Resource resource;

	@BeforeEach
	void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		Set<GrantedAuthority> authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority(Constants.ROLE_USER_NAME));
		UsernamePasswordAuthenticationToken userTest = new UsernamePasswordAuthenticationToken("test", "test",
				authorities);
		SecurityContextHolder.getContext().setAuthentication(userTest);
	}

	@Test
	void insertPost_insertOk() throws Exception {
		InsertPostReqDto req = new InsertPostReqDto();
		req.setContent("Test");
		req.setStatus(1);
		
		MockMultipartFile captureFile = new MockMultipartFile("files", "", "image/png", "Capture of post".getBytes());
		MockMultipartFile[] captureList = {captureFile};
		MockMultipartFile request = new MockMultipartFile("request", "", "application/json",
				objectMapper.writeValueAsString(req).getBytes());
		
		when(userService.getUserId()).thenReturn("userId");
		when(postService.insertPost(req,captureList, "userId")).thenReturn(new Post());
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.multipart(uri).file(request)
				.file(captureFile).contentType(MediaType.MULTIPART_FORM_DATA).content("")).andReturn();
		
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);

		String msg = mvcResult.getResponse().getContentAsString();
		assertEquals("Insert post successful!", msg);
	}
	
	@Test
	void insertPost_insertNg() throws Exception {
		InsertPostReqDto req = new InsertPostReqDto();
		req.setContent("Test");
		req.setStatus(1);
		
		MockMultipartFile captureFile = new MockMultipartFile("files", "", "image/png", "Capture of post".getBytes());
		MockMultipartFile[] captureList = {captureFile};
		MockMultipartFile request = new MockMultipartFile("request", "", "application/json",
				objectMapper.writeValueAsString(req).getBytes());
		
		when(userService.getUserId()).thenReturn("userId");
		when(postService.insertPost(req,captureList, "userId")).thenReturn(null);
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.multipart(uri).file(request)
				.file(captureFile).contentType(MediaType.MULTIPART_FORM_DATA).content("")).andReturn();
		
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);

		String msg = mvcResult.getResponse().getContentAsString();
		assertEquals("Insert post error!", msg);
	}
	
	@Test
	void getPost_Ok() throws Exception {
		List<GetPostResDto> res = new ArrayList<GetPostResDto>();
		when(postService.getPostCustom(eq("userId"), any(java.sql.Date.class), any(java.sql.Date.class), eq(10) )).thenReturn(res);
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
				.param("userId", "userId")
				.param("timeStart", "2021-01-01")
				.param("timeEnd", "2024-01-01")
				.param("numbersPost", "10")
				.contentType(MediaType.MULTIPART_FORM_DATA).content("")).andReturn();
		
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);

		String msg = mvcResult.getResponse().getContentAsString();
		assertEquals("[]", msg);
	}
	
	@Test
	void updatePost_postNotExists() throws Exception {
		UpdatePostReqDto req = new UpdatePostReqDto();	
		req.setContent("Test");
		req.setStatus(1);

		MockMultipartFile captureFile = new MockMultipartFile("files", "", "image/png", "Capture of post".getBytes());
		MockMultipartFile request = new MockMultipartFile("request", "", "application/json",
				objectMapper.writeValueAsString(req).getBytes());
		
		when(userService.getUserId()).thenReturn("userId");
		when(postService.findByPostIdAndUserId("postId", "userId")).thenReturn(null);
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.multipart(uri+"/{postId}","postId")
				.file(request)
				.file(captureFile)
				.contentType(MediaType.MULTIPART_FORM_DATA).content("")).andReturn();
		
		int status = mvcResult.getResponse().getStatus();
		assertEquals(404, status);
	}
	
	@Test
	void updatePost_postExists_updateOk() throws Exception {
		UpdatePostReqDto req = new UpdatePostReqDto();	
		req.setContent("Test");
		req.setStatus(1);

		Post post = new Post();
		MockMultipartFile captureFile = new MockMultipartFile("files", "", "image/png", "Capture of post".getBytes());
		MockMultipartFile[] captureList = {captureFile};
		MockMultipartFile request = new MockMultipartFile("request", "", "application/json",
				objectMapper.writeValueAsString(req).getBytes());
		
		when(userService.getUserId()).thenReturn("userId");
		when(postService.findByPostIdAndUserId("postId", "userId")).thenReturn(post);
		when(postService.updatePost(post, req, captureList)).thenReturn(post);
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.multipart(uri+"/{postId}","postId")
				.file(request)
				.file(captureFile)
				.contentType(MediaType.MULTIPART_FORM_DATA).content("")).andReturn();
		
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);

		String msg = mvcResult.getResponse().getContentAsString();
		assertEquals("Update post successfull!", msg);
	}
	
	@Test
	void updatePost_postExists_updateNg() throws Exception {
		UpdatePostReqDto req = new UpdatePostReqDto();	
		req.setContent("Test");
		req.setStatus(1);

		Post post = new Post();
		MockMultipartFile captureFile = new MockMultipartFile("files", "", "image/png", "Capture of post".getBytes());
		MockMultipartFile[] captureList = {captureFile};
		MockMultipartFile request = new MockMultipartFile("request", "", "application/json",
				objectMapper.writeValueAsString(req).getBytes());
		
		when(userService.getUserId()).thenReturn("userId");
		when(postService.findByPostIdAndUserId("postId", "userId")).thenReturn(post);
		when(postService.updatePost(post, req, captureList)).thenReturn(null);
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.multipart(uri+"/{postId}","postId")
				.file(request)
				.file(captureFile)
				.contentType(MediaType.MULTIPART_FORM_DATA).content("")).andReturn();
		
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);

		String msg = mvcResult.getResponse().getContentAsString();
		assertEquals("Update post error!", msg);
	}
	
	@Test
	void deletePost_postNotExists() throws Exception {	
		when(userService.getUserId()).thenReturn("userId");
		when(postService.findByPostIdAndUserId("postId", "userId")).thenReturn(null);
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(uri+"/{postId}","postId")
				.contentType(MediaType.MULTIPART_FORM_DATA).content("")).andReturn();
		
		int status = mvcResult.getResponse().getStatus();
		assertEquals(404, status);
	}
	
	@Test
	void deletePost_postExists() throws Exception {	
		when(userService.getUserId()).thenReturn("userId");
		when(postService.findByPostIdAndUserId("postId", "userId")).thenReturn(new Post());
		doNothing().when(postService).deletePost(any(Post.class));
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(uri+"/{postId}","postId")
				.contentType(MediaType.MULTIPART_FORM_DATA).content("")).andReturn();
		
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		
		String msg = mvcResult.getResponse().getContentAsString();
		assertEquals("Delete post successfull!", msg);
	}
	
	@Test
	void getPostTimeLine_Ok() throws Exception {	
		when(userService.getUserId()).thenReturn("userId");
		List<GetPostResDto> res = new ArrayList<>();
		when(postService.getPostTimeLine(10, userService.getUserId())).thenReturn(res);
		doNothing().when(postService).deletePost(any(Post.class));
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri+"/timeline")
				.param("numbers-post", "10")
				.contentType(MediaType.APPLICATION_JSON).content("")).andReturn();
		
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		
		String msg = mvcResult.getResponse().getContentAsString();
		assertEquals("[]", msg);
	}
	
	@Test
	void likePost_postNotExists() throws Exception {	
		when(userService.getUserId()).thenReturn("userId");
		LikePostReqDto request = new LikePostReqDto();
		request.setPostId("postId");
		
		when(postService.hasLike("userId", request.getPostId())).thenReturn(true);
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri+"/like")
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request))).andReturn();
		
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		
		String msg = mvcResult.getResponse().getContentAsString();
		assertEquals("User had liked!", msg);
	}
	
	@Test
	void likePost_postExists_likeOk() throws Exception {	
		when(userService.getUserId()).thenReturn("userId");
		LikePostReqDto request = new LikePostReqDto();
		request.setPostId("postId");
		
		when(postService.hasLike("userId", request.getPostId())).thenReturn(false);
		when(postService.likePost("userId", request.getPostId())).thenReturn(new Like());
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri+"/like")
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request))).andReturn();
		
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		
		String msg = mvcResult.getResponse().getContentAsString();
		assertEquals("Like post successful!", msg);
	}
	
	@Test
	void likePost_postExists_likeNg() throws Exception {	
		when(userService.getUserId()).thenReturn("userId");
		LikePostReqDto request = new LikePostReqDto();
		request.setPostId("postId");
		
		when(postService.hasLike("userId", request.getPostId())).thenReturn(false);
		when(postService.likePost("userId", request.getPostId())).thenReturn(null);
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri+"/like")
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request))).andReturn();
		
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		
		String msg = mvcResult.getResponse().getContentAsString();
		assertEquals("Like post error!", msg);
	}
	
	@Test
	void dislikePost_postNotExists() throws Exception {	
		when(userService.getUserId()).thenReturn("userId");
		LikePostReqDto request = new LikePostReqDto();
		request.setPostId("postId");
		
		when(postService.hasLike("userId", request.getPostId())).thenReturn(false);
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(uri+"/dislike")
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request))).andReturn();
		
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		
		String msg = mvcResult.getResponse().getContentAsString();
		assertEquals("User had not like!", msg);
	}
	
	@Test
	void likePost_postExists_dislikeOk() throws Exception {	
		when(userService.getUserId()).thenReturn("userId");
		LikePostReqDto request = new LikePostReqDto();
		request.setPostId("postId");
		
		when(postService.hasLike("userId", request.getPostId())).thenReturn(true);
		doNothing().when(postService).dislikePost("userId", "postId");
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(uri+"/dislike")
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request))).andReturn();
		
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		
		String msg = mvcResult.getResponse().getContentAsString();
		assertEquals("Unlike successful!", msg);
	}
	
}
