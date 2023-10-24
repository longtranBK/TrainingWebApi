package com.example.demo.dto.response;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class GetPostResDto {

	private String postId;
	
	private String content;
	
	private int countLikes;
	
	private Timestamp createTs;
	
	private Timestamp updateTs;
	
	private List<String> captureUrlList = new ArrayList<>();
	
}

