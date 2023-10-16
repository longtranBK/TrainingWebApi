package com.example.demo.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
public class FileServiceImplTest {
	@Autowired
	private FileServiceImpl fileServiceImpl;

	@Test
	void save_withInput_returnPath() {
		byte[] content = "test".getBytes();
		MultipartFile file = new MockMultipartFile("name", "file.txt", "text/plain", content);
		String result = fileServiceImpl.save(file, "test/");
		assertEquals("D:/uploads/test/file.txt", result);
	}
	
	@Test
	void save_withInput_returnBlank() {
		MultipartFile file = null;
		String result = fileServiceImpl.save(file, "test/");
		assertEquals("", result);
	}
}
