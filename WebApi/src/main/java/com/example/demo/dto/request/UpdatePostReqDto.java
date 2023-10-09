package com.example.demo.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import lombok.Data;

@Data
@Validated
public class UpdatePostReqDto {

	@NotBlank(message = "The content is required.")
	private String content;

	@NotNull(message = "The status is required.")
	private int status;
}
