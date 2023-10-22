package com.example.demo.dto.response;

import java.util.ArrayList;

import lombok.Data;

@Data
public class PostUpdateResDto {

	private String content;
	
	private ArrayList<String> postImageUrlList = new ArrayList<>();
}
