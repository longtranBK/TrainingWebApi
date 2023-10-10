package com.example.demo.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class LikePostReqDto {
	
	@Size(max = 36)
	@NotBlank(message = "The postId is required.")
	private String postId;

}
