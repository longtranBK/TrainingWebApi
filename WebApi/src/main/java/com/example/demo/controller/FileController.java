package com.example.demo.controller;

import java.io.IOException;

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

	private UserService userService;

	private FileService fileService;

	@Operation(summary = "Upload avatar image")
	@PostMapping(value = "/upload", consumes = "multipart/form-data")
	public ResponseEntity<?> uploadImage(
			@RequestPart(value = "files", required = false) MultipartFile file)	throws IOException {

		if (file == null) {
			return ResponseEntity.ok().body("Please select file!");
		}
		if (fileService.checkJPEG(file)) {
			return ResponseEntity.ok().body("Please select file .jpg or .png!");
		}
		String subPath = "/" + userService.getUserId() + "/";
		return ResponseEntity.ok().body(fileService.saveOneFile(file, subPath));
	}
	
}
