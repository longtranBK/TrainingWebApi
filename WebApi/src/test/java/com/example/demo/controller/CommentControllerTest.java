package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
//import org.springframework.security.test.context.support.*;

import com.example.demo.constant.Constants;
import com.example.demo.dto.request.InsertCommentReqDto;
import com.example.demo.entity.Post;
import com.example.demo.service.CommentService;
import com.example.demo.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
public class CommentControllerTest {

	private String uri = "/api/comments";

	private MockMvc mockMvc;

	Authentication authentication = Mockito.mock(Authentication.class);

	@Autowired
	WebApplicationContext webApplicationContext;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private PostService postService;

	@MockBean
	private CommentService commentService;
	
	@BeforeEach
	void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		Set<GrantedAuthority> authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority(Constants.ROLE_USER_NAME));
		UsernamePasswordAuthenticationToken userTest = new UsernamePasswordAuthenticationToken("test",
				"test", authorities);
		SecurityContextHolder.getContext().setAuthentication(userTest);
	}

	@Test
	void insertComment_postNotExists() throws Exception {
		InsertCommentReqDto request = new InsertCommentReqDto();
		request.setPostId("1");
		request.setContent("test");

		when(postService.findByPostId(request.getPostId())).thenReturn(null);
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);

		String msg = mvcResult.getResponse().getContentAsString();
		assertEquals("Post not found!", msg);

	}

	@Test
	void insertComment_postExists() throws Exception {
		InsertCommentReqDto request = new InsertCommentReqDto();
		request.setPostId("1");
		request.setContent("test");

		when(postService.findByPostId(request.getPostId())).thenReturn(new Post());
		doNothing().when(commentService).insertComment(request,"");
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);

		String msg = mvcResult.getResponse().getContentAsString();
		assertEquals("Insert comment successful!", msg);

	}
}
