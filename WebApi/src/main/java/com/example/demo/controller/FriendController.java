package com.example.demo.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.constant.Constants;
import com.example.demo.dto.request.FriendReqDto;
import com.example.demo.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Friend", description = "API thao tác với quan hệ friend")
@RestController
@RequestMapping("api/friends")
public class FriendController {
	
	@Autowired
	private UserService userService;
	
	@Operation(summary = "Add friend")
	@PostMapping(value = { "" })
	@Secured(Constants.ROLE_USER_NAME)
	public ResponseEntity<?> addFriend(@Valid @RequestBody FriendReqDto request) {

		if(userService.isFriend(request.getUserIdCurrent(), request.getUserIdFriend())) {
			return ResponseEntity.ok().body("User was friend!");
		}
		userService.addFriend(request.getUserIdCurrent(), request.getUserIdFriend());
		
		return ResponseEntity.ok().body("Add friend successful!");
	}
	
	@Operation(summary = "Unfriend")
	@DeleteMapping(value = { "" })
	@Secured(Constants.ROLE_USER_NAME)
	public ResponseEntity<?> unFriend(@Valid @RequestBody FriendReqDto request) {

		if(!userService.isFriend(request.getUserIdCurrent(), request.getUserIdFriend())) {
			return ResponseEntity.ok().body("User was not friend!");
		}
		userService.unFriend(request.getUserIdCurrent(), request.getUserIdFriend());
		
		return ResponseEntity.ok().body("Unfriend successful!");
	}
}
