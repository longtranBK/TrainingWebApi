package com.example.demo.controller;

import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.constant.Constants;
import com.example.demo.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Friend", description = "API thao tác với quan hệ friend")
@Validated
@RestController
@RequestMapping("api/friends")
public class FriendController {

	@Autowired
	private UserService userService;

	@Operation(summary = "Add friend")
	@PostMapping(value = { "/{userIdFriend}" })
	@Secured(Constants.ROLE_USER_NAME)
	public ResponseEntity<?> addFriend(
			@PathVariable(required = true) @Size(max = 36) String userIdFriend) {

		String userIdCurrent = userService.getUserId();

		if (userService.isFriend(userIdCurrent, userIdFriend)) {
			return ResponseEntity.ok().body("User was friend!");
		}

		if(userService.getByUserId(userIdFriend) == null) {
			return ResponseEntity.ok().body("Userfriend not found!");
		}
		
		if(!userIdFriend.equals(userIdCurrent)) {
			if (userService.addFriend(userIdCurrent, userIdFriend) != null) {
				return ResponseEntity.ok().body("Add friend successful!");
			}
		}
		return ResponseEntity.internalServerError().body("Add friend error!");
	}

	@Operation(summary = "Unfriend")
	@DeleteMapping(value = { "{userIdFriend}" })
	@Secured(Constants.ROLE_USER_NAME)
	public ResponseEntity<?> unFriend(
			@PathVariable(required = true) @Size(max = 36) String userIdFriend) {

		String userIdCurrent = userService.getUserId();
		
		if(userService.getByUserId(userIdFriend) == null) {
			return ResponseEntity.ok().body("Userfriend not found!");
		}
		
		if (!userService.isFriend(userIdCurrent, userIdFriend)) {
			return ResponseEntity.ok().body("User was not friend!");
		}
		userService.unFriend(userIdCurrent, userIdFriend);

		return ResponseEntity.ok().body("Unfriend successful!");
	}
}
