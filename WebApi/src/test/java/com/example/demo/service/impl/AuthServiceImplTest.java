package com.example.demo.service.impl;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.example.demo.repository.UserRepository;

@SpringBootTest
public class AuthServiceImplTest {
	@MockBean
	private UserRepository userRepository;

	@Autowired
	private AuthServiceImpl authServiceImpl;

	@Test
	void isActive_returnResult() {
		when(userRepository.getActive(any(String.class))).thenReturn(true);
		boolean result = authServiceImpl.isActive("name");
		assertTrue(result);
	}
}
