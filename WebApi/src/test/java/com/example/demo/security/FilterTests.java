package com.example.demo.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.example.demo.security.jwt.JwtUtils;
import com.example.demo.service.UserService;

@SpringBootTest
public class FilterTests {
	private String uri = "/api/users";

	private MockMvc mockMvc;

	@Autowired
	WebApplicationContext webApplicationContext;

	@Autowired
	AuthTokenFilter authTokenFilter;

	@MockBean
	private UserService userService;

	@MockBean
	private JwtUtils jwtUtils; 
	
	@BeforeEach
	void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	void doFilterInternal_Ok() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("Authorization", "Bearer jwttoken");
	    request.setRequestURI(uri);
	    
	    MockHttpServletResponse response = new MockHttpServletResponse();
	    MockFilterChain filterChain = new MockFilterChain();
	    
	    when(jwtUtils.validateJwtToken(any(String.class))).thenReturn(true);
	    when(jwtUtils.getUserNameFromJwtToken(any(String.class))).thenReturn("longth@gmail.com");
	    authTokenFilter.doFilter(request, response, filterChain);
	    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
	}
}
