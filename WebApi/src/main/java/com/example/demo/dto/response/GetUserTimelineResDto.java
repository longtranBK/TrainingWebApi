package com.example.demo.dto.response;

import lombok.Data;

@Data
public class GetUserTimelineResDto {
	
	private String userId;

	private String fullName;
	
	private String avatarUrl;
}
