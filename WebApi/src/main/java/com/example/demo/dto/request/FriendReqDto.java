package com.example.demo.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class FriendReqDto {

	@Size(max = 36)
	@NotBlank(message = "The userIdCurrent is required.")
	private String userIdCurrent;
	
	@Size(max = 36)
	@NotBlank(message = "The userIdFriend is required.")
	private String userIdFriend;
}
