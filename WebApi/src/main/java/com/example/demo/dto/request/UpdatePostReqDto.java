package com.example.demo.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.validation.annotation.Validated;

import lombok.Data;

@Data
@Validated
public class UpdatePostReqDto {
	
	@Size(max = 5000)
	@NotBlank(message = "The content is required.")
	private String content;

	@Size(max = 1)
	@NotNull(message = "The status is required.")
	private String status;
}
