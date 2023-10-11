package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import com.example.demo.dto.request.UpdateUserInforReqDto;
import com.example.demo.dto.response.UserInforResDto;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
public class UsersControllerTests {
	private String uri = "/api/users";

	private MockMvc mockMvc;

	@Autowired
	WebApplicationContext webApplicationContext;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private UserService userService;

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
	void getUserDetails_userNotExists() throws Exception {
		when(userService.getByUserId("userId")).thenReturn(null);
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri+"/user-details/{userId}", "userId")
				.contentType(MediaType.APPLICATION_JSON).content("")).andReturn();
		
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		
		String msg = mvcResult.getResponse().getContentAsString();
		assertEquals("User not found!", msg);
	}

	@Test
	void getUserDetails_userExists() throws Exception {
		
		when(userService.getByUserId("userId")).thenReturn(new User());
		when(userService.getUserInfor("userId")).thenReturn(any());
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri+"/user-details/{userId}", "userId")
				.contentType(MediaType.APPLICATION_JSON).content("")).andReturn();
		
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
	}
	
	@Test
	void updateUserInfor_updateOk() throws Exception {
		UpdateUserInforReqDto req = new UpdateUserInforReqDto();
		req.setFullName("test");
		req.setSex(0);
		req.setFavorites("test");
		req.setStudyAt("test");
		req.setWorkingAt("test");
		req.setDateOfBirth("2000-01-01");
		req.setOtherInfor("test");
		
		User user = new User();
		when(userService.getUserId()).thenReturn("userId");
		when(userService.getByUserId("userId")).thenReturn(user);
		when(userService.updateUserInfor(any(User.class), any(UpdateUserInforReqDto.class), any(MockMultipartFile.class) )).thenReturn(user);
		
		MockMultipartFile captureFile = new MockMultipartFile("file", "", "image/png", "Capture of post".getBytes());
		MockMultipartFile request = new MockMultipartFile("request", "", "application/json",
				objectMapper.writeValueAsString(req).getBytes());
		
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.multipart(uri+"/user-details")
				.file(request)
				.file(captureFile)
				.contentType(MediaType.APPLICATION_JSON).content("")).andReturn();
		
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		
		String msg = mvcResult.getResponse().getContentAsString();
		assertEquals("User details updated!", msg);
	}
	
	@Test
	void updateUserInfor_updateNg() throws Exception {
		UpdateUserInforReqDto req = new UpdateUserInforReqDto();
		req.setFullName("test");
		req.setSex(0);
		req.setFavorites("test");
		req.setStudyAt("test");
		req.setWorkingAt("test");
		req.setDateOfBirth("2000-01-01");
		req.setOtherInfor("test");
		
		User user = new User();
		when(userService.getUserId()).thenReturn("userId");
		when(userService.getByUserId("userId")).thenReturn(user);
		when(userService.updateUserInfor(any(User.class), any(UpdateUserInforReqDto.class), any(MockMultipartFile.class) )).thenReturn(null);
		
		MockMultipartFile captureFile = new MockMultipartFile("file", "", "image/png", "Capture of post".getBytes());
		MockMultipartFile request = new MockMultipartFile("request", "", "application/json",
				objectMapper.writeValueAsString(req).getBytes());
		
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.multipart(uri+"/user-details")
				.file(request)
				.file(captureFile)
				.contentType(MediaType.APPLICATION_JSON).content("")).andReturn();
		
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		
		String msg = mvcResult.getResponse().getContentAsString();
		assertEquals("User details error!", msg);
	}
}
