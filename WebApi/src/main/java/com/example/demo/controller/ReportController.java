package com.example.demo.controller;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.constant.Constants;
import com.example.demo.service.ReportService;
import com.example.demo.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Report", description = "API thao tác với report")
@Validated
@RestController
@RequestMapping("/api/report")
public class ReportController {
	
	@Autowired
	private ReportService reportService;
	
	@Autowired
	private UserService userService;
	
	@Operation(summary = "Download report")
	@GetMapping("/download")
	@Secured(Constants.ROLE_USER_NAME)
	public ResponseEntity<Resource> getFile(
			@RequestParam(value = "timeStart", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) java.util.Date timeStart,
			@RequestParam(value = "timeEnd", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) java.util.Date timeEnd,
			@RequestParam(value = "numbersPost", required = true) int numbersPost) {
		
		Date start = new java.sql.Date(timeStart.getTime());
		Date end = new java.sql.Date(timeEnd.getTime());
		
		String filename = "Report.xlsx";
		InputStreamResource file = new InputStreamResource(reportService.loadData(userService.getUserId(), start, end, numbersPost));

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
				.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(file);
	}
}
