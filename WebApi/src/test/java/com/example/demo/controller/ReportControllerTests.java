package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
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
import com.example.demo.service.ReportService;
import com.example.demo.service.UserService;

@SpringBootTest
public class ReportControllerTests {

	private String uri = "/api/report/download";

	private MockMvc mockMvc;

	@Autowired
	WebApplicationContext webApplicationContext;

	@MockBean
	private UserService userService;


	@MockBean
	private ReportService reportService;

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
	void getFile_Ok() throws Exception {
		when(userService.getUserId()).thenReturn("userId");
//		when(reportService.loadData(eq("userId"), any(java.sql.Date.class), any(java.sql.Date.class), eq(10))).thenReturn(new ByteArrayInputStream("TestData".getBytes()));
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
				.param("timeStart", "2021-01-01")
				.param("timeEnd", "2024-01-01")
				.param("numbersPost", "10")
				.contentType(MediaType.MULTIPART_FORM_DATA).content("")).andReturn();
		
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		
	}
}
