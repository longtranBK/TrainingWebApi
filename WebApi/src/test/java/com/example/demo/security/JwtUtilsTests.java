package com.example.demo.security;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.security.jwt.JwtUtils;

import io.jsonwebtoken.ExpiredJwtException;

@SpringBootTest
public class JwtUtilsTests {

	@Autowired
	private JwtUtils jwtUtils;

	@Test
	void generateJwtToken_Ok() {
		String jwtToken = jwtUtils.generateJwtToken("username");
		assertFalse(jwtToken.isEmpty());
	}
	
	@Test
	void getUserNameFromJwtToken_Ok() {
		assertThrows(ExpiredJwtException.class,
	            ()->{
	            	jwtUtils.getUserNameFromJwtToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsb25ndGhAZ21haWwuY29tIiwiaWF0IjoxNjk3MDQwMjk1LCJleHAiOjE2OTcxMjY2OTV9.ThkwDGuImh7QS_uJTFQN4WhDA_IRfyofAOEEhQ_bpf0");
	            });
	}
	
	@Test
	void validateJwtToken_Ok() {
		boolean validate = jwtUtils.validateJwtToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsb25ndGhAZ21haWwuY29tIiwiaWF0IjoxNjk3MDQwMjk1LCJleHAiOjE2OTcxMjY2OTV9.ThkwDGuImh7QS_uJTFQN4WhDA_IRfyofAOEEhQ_bpf0");
		assertFalse(validate);
	}
}