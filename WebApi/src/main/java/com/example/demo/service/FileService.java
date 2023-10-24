package com.example.demo.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
	/**
	 * Save file and return file path
	 * 
	 * @param file
	 * @param path
	 * @return path file
	 */
	public String saveOneFile(MultipartFile file, String pathFolder) throws IOException;

	/**
	 * 
	 * @param file
	 * @return
	 */
	public boolean checkJPEG(MultipartFile file);

}
