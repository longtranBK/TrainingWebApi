package com.example.demo.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

	/**
	 * Init file service when start application
	 */
	public void init();

	/**
	 * Save file upload
	 * 
	 * @param file
	 * @return url file
	 */
	public String save(MultipartFile file);

}
