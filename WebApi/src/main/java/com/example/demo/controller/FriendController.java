package com.example.demo.controller;

import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Friend", description = "API thao tác với quan hệ friend")
@Validated
@RestController
@RequestMapping("/v1")
public class FriendController {

	@Autowired
	private UserService userService;

	@Operation(summary = "Send request friend")
	@PostMapping(value = "/request-friend/{userIdFriend}")
	public ResponseEntity<?> addFriend(
			@PathVariable(required = true) @Size(max = 36) String userIdFriend) {
		String userIdCurrent = userService.getUserId();
		if (userIdFriend.equals(userIdCurrent)) {
			return ResponseEntity.ok().body("UserId have to not current user!");
		}

		if (userService.getByUserId(userIdFriend) == null) {
			return ResponseEntity.ok().body("User not found!");
		}

		if (userService.hasSendRequest(userIdCurrent, userIdFriend)) {
			return ResponseEntity.ok().body("Request had sent!");
		}

		if (userService.isFriend(userIdCurrent, userIdFriend)) {
			return ResponseEntity.ok().body("User was friend!");
		}

		if (userService.sentRequestFriend(userIdFriend, userIdCurrent) != null) {
			return ResponseEntity.ok().body("Sent request successful!");
		} else {
			return ResponseEntity.internalServerError().body("Sent request error!");
		}
	}

	@Operation(summary = "Cancel request friend")
	@DeleteMapping(value = "/request-friend/{userIdFriend}")
	public ResponseEntity<?> cancelRequestFriend(
			@PathVariable(required = true) @Size(max = 36) String userIdFriend) {
		
		String userIdCurrent = userService.getUserId();
		if (userIdFriend.equals(userIdCurrent)) {
			return ResponseEntity.ok().body("UserId have to not current user!");
		}

		if (userService.getByUserId(userIdFriend) == null) {
			return ResponseEntity.ok().body("User not found!");
		}

		if (userService.cancelSendRequest(userIdFriend, userIdCurrent) != 0) {
			return ResponseEntity.ok().body("Cancel request success!");
		} else {
			return ResponseEntity.ok().body("Cancel request error!");
		}
	}
	
	@Operation(summary = "Access request friend")
	@PutMapping(value = "/request-friend/{userIdFriend}")
	public ResponseEntity<?> acceptFriend(
			@PathVariable(required = true) @Size(max = 36) String userIdFriend) {
		
		String userIdCurrent = userService.getUserId();
		if (userIdFriend.equals(userIdCurrent)) {
			return ResponseEntity.ok().body("UserId have to not current user!");
		}

		if (userService.getByUserId(userIdFriend) == null) {
			return ResponseEntity.ok().body("User not found!");
		}

		if (userService.isFriend(userIdCurrent, userIdFriend)) {
			return ResponseEntity.ok().body("User was friend!");
		}

		if (!userService.hasSendRequest(userIdCurrent, userIdFriend)) {
			return ResponseEntity.ok().body("Request friend not found!");
		}

		if (userService.acceptFriend(userIdFriend, userIdCurrent) != null) {
			return ResponseEntity.ok().body("Accept request success!");
		} else {
			return ResponseEntity.ok().body("Accept request error!");
		}
	}
	
	@Operation(summary = "Unfriend")
	@DeleteMapping(value = "/friends/{userIdFriend}")
	public ResponseEntity<?> unFriend(
			@PathVariable(required = true) @Size(max = 36) String userIdFriend) {
		
		String userIdCurrent = userService.getUserId();
		if (userIdFriend.equals(userIdCurrent)) {
			return ResponseEntity.ok().body("UserId have to not current user!");
		}

		if (userService.getByUserId(userIdFriend) == null) {
			return ResponseEntity.ok().body("User not found!");
		}

		if (!userService.isFriend(userIdCurrent, userIdFriend)) {
			return ResponseEntity.ok().body("User was not friend!");
		}
		userService.unFriend(userIdFriend, userIdCurrent);
		return ResponseEntity.ok().body("Unfriend successful!");
		
	}
}
