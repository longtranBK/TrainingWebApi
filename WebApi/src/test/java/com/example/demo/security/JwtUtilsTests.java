package com.example.demo.security;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.security.jwt.JwtUtils;

@SpringBootTest
public class JwtUtilsTests {

	@Autowired
	private JwtUtils jwtUtils;

	@Test
	void generateJwtToken_Ok() {
		String jwtToken = jwtUtils.generateJwtToken("username");
		assertTrue(!jwtToken.isEmpty());
	}
	
	@Test
	void getUserNameFromJwtToken_Ok() {
		String username = jwtUtils.getUserNameFromJwtToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsb25ndGhAZ21haWwuY29tIiwiaWF0IjoxNjk3MDQwMjk1LCJleHAiOjE2OTcxMjY2OTV9.ThkwDGuImh7QS_uJTFQN4WhDA_IRfyofAOEEhQ_bpf0");
		assertTrue(!username.isEmpty());
	}
	
	@Test
	void validateJwtToken_Ok() {
		boolean validate = jwtUtils.validateJwtToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsb25ndGhAZ21haWwuY29tIiwiaWF0IjoxNjk3MDQwMjk1LCJleHAiOjE2OTcxMjY2OTV9.ThkwDGuImh7QS_uJTFQN4WhDA_IRfyofAOEEhQ_bpf0");
		assertTrue(validate);
	}
}