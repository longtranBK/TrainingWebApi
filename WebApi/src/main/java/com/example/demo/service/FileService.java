package com.example.demo.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

	/**
	 * Save file upload
	 * 
	 * @param file
	 * @return url file
	 */
	public String save(MultipartFile file);

}
