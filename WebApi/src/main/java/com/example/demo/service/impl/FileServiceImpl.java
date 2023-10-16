package com.example.demo.service.impl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.service.FileService;

@Service
public class FileServiceImpl implements FileService {

	@Value("${upload.path}")
	private String uploadPath;

	@Override
	public String save(MultipartFile file, String subPath) {
		try {
			if(file == null) {
				return "";
			}
			String pathStr = uploadPath + subPath;
			Files.createDirectories(Paths.get(pathStr));
			Path path = Paths.get(pathStr + file.getOriginalFilename());
			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			System.gc();
			return pathStr + file.getOriginalFilename();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}
