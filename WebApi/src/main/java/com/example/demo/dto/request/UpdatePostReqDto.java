package com.example.demo.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UpdatePostReqDto {
	
	@Schema(type = "string", example = "Content of post")
	@Size(max = 5000)
	@NotBlank(message = "The content is required.")
	private String content;
}
