package com.example.demo.service;

import org.springframework.core.io.Resource;
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

	/**
	 * Load file upload
	 * 
	 * @param filename
	 * @return Respurce
	 */
	public Resource load(String filename);

}
