package com.example.demo.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class InsertCommentReqDto {
	
	@Schema(type = "string", example = "307dd00d-7c25-46de-a8b4-c96e17996e39")
	@Size(max = 36)
	@NotBlank(message = "The postId is required.")
	private String postId;
	
	@Schema(type = "string", example = "Content of comment")
	@Size(max = 1024)
	@NotBlank(message = "The content is required.")
	private String content;
}
