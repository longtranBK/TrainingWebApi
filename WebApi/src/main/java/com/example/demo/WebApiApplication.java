package com.example.demo;

import javax.annotation.Resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.demo.service.FileService;

@SpringBootApplication
public class WebApiApplication {

	@Resource
	FileService fileService;

	public static void main(String[] args) {
		SpringApplication.run(WebApiApplication.class, args);
	}

}
