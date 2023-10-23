package com.example.demo.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.security.jwt.AuthEntryPointJwt;
import com.example.demo.service.FileService;

@Service
public class FileServiceImpl implements FileService {

	private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

	@Value("${upload.path}")
	private String uploadPath;

	@Override
	public List<String> saveMultifile(MultipartFile[] files, String subPath) {
		if (files == null || files.length == 0) {
			return null;
		}
		List<String> fileNameList = new ArrayList<>();
		Arrays.asList(files).stream().forEach(file -> {
			try {
				fileNameList.add(saveOneFile(file, subPath));
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		});
		return fileNameList;
	}

	@Override
	public String saveOneFile(MultipartFile file, String subPath) throws IOException {
		String pathStr = uploadPath + subPath;
		Files.createDirectories(Paths.get(pathStr));
		Path path = Paths.get(pathStr + file.getOriginalFilename());
		Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
		System.gc();
		return pathStr + file.getOriginalFilename();
	}

	@Override
	public boolean checkJPEG(MultipartFile file) {
		String fileName = file.getName().toUpperCase();
		return fileName.endsWith(".JPG") || fileName.endsWith(".PNG");
	}
}
