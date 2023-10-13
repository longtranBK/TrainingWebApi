package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
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
import com.example.demo.entity.User;
import com.example.demo.entity.UserFriend;
import com.example.demo.service.UserService;

@SpringBootTest
public class FriendControllerTests {
	
	private String uri = "/api/friends";
	
	private MockMvc mockMvc;

	@Autowired
	WebApplicationContext webApplicationContext;

	@MockBean
	private UserService userService;
	
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
	void addFriend_hadFriend() throws Exception {

		when(userService.getUserId()).thenReturn("userId");
		when(userService.isFriend("userId", "userIdFriend")).thenReturn(true);
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri+"/{userIdFriend}","userIdFriend").contentType(MediaType.APPLICATION_JSON)
				.content("")).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);

		String msg = mvcResult.getResponse().getContentAsString();
		assertEquals("User was friend!", msg);
	}
	
	@Test
	void addFriend_notFriend_notExist() throws Exception {

		when(userService.getUserId()).thenReturn("userId");
		when(userService.isFriend("userId", "userIdFriend")).thenReturn(false);
		when(userService.getByUserId("userIdFriend")).thenReturn(null);
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri+"/{userIdFriend}","userIdFriend").contentType(MediaType.APPLICATION_JSON)
				.content("")).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);

		String msg = mvcResult.getResponse().getContentAsString();
		assertEquals("Userfriend not found!", msg);
	}
	
	@Test
	void addFriend_notFriend_exist_isCurrentUser() throws Exception {

		when(userService.getUserId()).thenReturn("userIdFriend");
		when(userService.isFriend("userIdFriend", "userIdFriend")).thenReturn(false);
		when(userService.getByUserId("userIdFriend")).thenReturn(new User());
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri+"/{userIdFriend}","userIdFriend").contentType(MediaType.APPLICATION_JSON)
				.content("")).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(500, status);

	}
	
	@Test
	void addFriend_notFriend_exist_notCurrentUser_Ok() throws Exception {

		when(userService.getUserId()).thenReturn("userId");
		when(userService.isFriend("userId", "userIdFriend")).thenReturn(false);
		when(userService.getByUserId("userIdFriend")).thenReturn(new User());
		when(userService.addFriend("userId", "userIdFriend")).thenReturn(new UserFriend());
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri+"/{userIdFriend}","userIdFriend").contentType(MediaType.APPLICATION_JSON)
				.content("")).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);

		String msg = mvcResult.getResponse().getContentAsString();
		assertEquals("Add friend successful!", msg);
	}
	
	@Test
	void addFriend_notFriend_exist_notCurrentUser_Ng() throws Exception {

		when(userService.getUserId()).thenReturn("userId");
		when(userService.isFriend("userId", "userIdFriend")).thenReturn(false);
		when(userService.getByUserId("userIdFriend")).thenReturn(new User());
		when(userService.addFriend("userId", "userIdFriend")).thenReturn(null);
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri+"/{userIdFriend}","userIdFriend").contentType(MediaType.APPLICATION_JSON)
				.content("")).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(500, status);

	}
	
	@Test
	void unFriend_friendNotExists() throws Exception {
		when(userService.getUserId()).thenReturn("userId");
		when(userService.getByUserId("userIdFriend")).thenReturn(null);
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(uri+"/{userIdFriend}","userIdFriend").contentType(MediaType.APPLICATION_JSON)
				.content("")).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);

		String msg = mvcResult.getResponse().getContentAsString();
		assertEquals("Userfriend not found!", msg);
	}
	
	@Test
	void unFriend_friendExists_notFriend() throws Exception {
		when(userService.getUserId()).thenReturn("userId");
		when(userService.getByUserId("userIdFriend")).thenReturn(new User());
		when(userService.isFriend("userId", "userIdFriend")).thenReturn(false);
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(uri+"/{userIdFriend}","userIdFriend").contentType(MediaType.APPLICATION_JSON)
				.content("")).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);

		String msg = mvcResult.getResponse().getContentAsString();
		assertEquals("User was not friend!", msg);
	}
	
	@Test
	void unFriend_friendExists_friend() throws Exception {
		when(userService.getUserId()).thenReturn("userId");
		when(userService.getByUserId("userIdFriend")).thenReturn(new User());
		when(userService.isFriend("userId", "userIdFriend")).thenReturn(true);
		doNothing().when(userService).unFriend("userId", "userIdFriend");
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(uri+"/{userIdFriend}","userIdFriend").contentType(MediaType.APPLICATION_JSON)
				.content("")).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);

		String msg = mvcResult.getResponse().getContentAsString();
		assertEquals("Unfriend successful!", msg);
	}
}
