package com.example.demo.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

	/**
	 * 
	 * @param files
	 * @param subPath
	 * @return
	 */
	public List<String> saveMultifile(MultipartFile[] files, String subPath);

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
