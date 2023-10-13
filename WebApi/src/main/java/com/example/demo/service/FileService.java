package com.example.demo.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

	/**
	 * Save file and return file path
	 * 
	 * @param file
	 * @param userId
	 * @return path file
	 */
	public String save(MultipartFile file, String userId);

}
