package com.example.demo.controller;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.service.FileService;
import com.example.demo.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "File", description = "API thao tác với file")
@Validated
@RestController
@RequestMapping("/v1/files/")
public class FileController {

	@Autowired
	private UserService userService;

	@Autowired
	private FileService fileService;

	@Operation(summary = "Upload avatar image")
	@PostMapping(value = "/upload", consumes = "multipart/form-data")
	public ResponseEntity<?> uploadImage(
			@RequestPart(value = "file", required = true) MultipartFile fileImage)	throws IOException {

		if (fileImage == null) {
			return ResponseEntity.ok().body("Please select file!");
		}
		if (!fileService.checkJPEG(fileImage)) {
			return ResponseEntity.badRequest().body("Please select file .jpg or .png!");
		}
		String subPath = userService.getUserId() + "/";
		String linkFile = fileService.saveOneFile(fileImage, subPath);
		Map<String, Object> responseBody = new LinkedHashMap<>();
		responseBody.put("Path file: ", linkFile);
		responseBody.put("msg", "Upload file success!");
		return ResponseEntity.ok().body(responseBody);
	}
	
}
