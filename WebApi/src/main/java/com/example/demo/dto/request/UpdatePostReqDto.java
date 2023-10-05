package com.example.demo.dto.request;

import java.util.List;

import org.springframework.validation.annotation.Validated;

import lombok.Data;

@Data
@Validated
public class UpdatePostReqDto {

	private String content;

	private int status;

	private List<String> captureUrlList;
}
