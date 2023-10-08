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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.constant.Constants;
import com.example.demo.service.ReportService;

@Controller
@RequestMapping("/api/report")
public class ReportController {
	
	@Autowired
	private ReportService reportService;

	@GetMapping("/download")
	@Secured(Constants.ROLE_USER_NAME)
	public ResponseEntity<Resource> getFile(
			@RequestParam("userId") String userId,
			@RequestParam("timeStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) java.util.Date timeStart,
			@RequestParam("timeEnd") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) java.util.Date timeEnd,
			@RequestParam("numbersPost") int numbersPost) {
		
		Date start = new java.sql.Date(timeStart.getTime());
		Date end = new java.sql.Date(timeEnd.getTime());
		
		String filename = "Report.xlsx";
		InputStreamResource file = new InputStreamResource(reportService.loadData(userId, start, end, numbersPost));

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
				.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(file);
	}
}
