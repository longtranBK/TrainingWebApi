package com.example.demo;

import javax.annotation.Resource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.demo.service.FileService;

@SpringBootApplication
public class WebApiApplication implements CommandLineRunner {

	@Resource
	FileService fileService;

	public static void main(String[] args) {
		SpringApplication.run(WebApiApplication.class, args);
	}

	@Override
	public void run(String... arg) throws Exception {
		fileService.init();
	}
}
