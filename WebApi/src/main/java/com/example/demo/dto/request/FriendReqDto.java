package com.example.demo.dto.request;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class FriendReqDto {

	@NotBlank(message = "The userIdCurrent is required.")
	private String userIdCurrent;
	
	@NotBlank(message = "The userIdFriend is required.")
	private String userIdFriend;
}
