package com.example.demo.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
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
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.example.demo.constant.Constants;
import com.example.demo.dto.request.InsertPostReqDto;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
public class CustomExceptionHandlerTests {
	
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	WebApplicationContext webApplicationContext;

	@Autowired
	private CustomExceptionHandler customExceptionHandler;

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
	void handleAll() throws Exception {
		Exception ex = new  Exception("Error");
		MockHttpServletRequest servletRequest = new MockHttpServletRequest();
		servletRequest.setServerName("http://localhost:8080");
		servletRequest.setRequestURI("/api/users/user-details/1");
		ServletWebRequest servletWebRequest = new ServletWebRequest(servletRequest);
		
		ResponseEntity<Object> obj = customExceptionHandler.handleAll(ex, servletWebRequest);
		
		assertNotEquals(null, obj);
	}
	
	@Test
	void handleArgumentException() throws Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/posts")
				.param("userId", "userId")
				.param("timeStart", "2021")
				.param("timeEnd", "2024-01-01")
				.param("numbersPost", "10")
				.contentType(MediaType.MULTIPART_FORM_DATA).content("")).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(400, status);
	}
	
	@Test
	void handleArgumentNotValidException() throws Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/posts")
				.param("userId", "0123456789012345678901234567890123456789")
				.param("timeStart", "2021-01-01")
				.param("timeEnd", "2024-01-01")
				.param("numbersPost", "10")
				.contentType(MediaType.MULTIPART_FORM_DATA).content("")).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(400, status);
	}
	
	@Test
	void handleConstraintViolationException() throws Exception {
		InsertPostReqDto req = new InsertPostReqDto();
		req.setContent("Test123123");
		MockMultipartFile request = new MockMultipartFile("request", "", "application/json",
				objectMapper.writeValueAsString(req).getBytes());
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.multipart("/api/posts")
				.file(request).contentType(MediaType.MULTIPART_FORM_DATA).content("")).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(400, status);
	}
}
