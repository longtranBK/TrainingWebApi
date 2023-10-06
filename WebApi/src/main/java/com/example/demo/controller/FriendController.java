package com.example.demo.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.request.FriendReqDto;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("api/friends")
public class FriendController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping(value = { "" })
	public ResponseEntity<?> addFriend(@Valid @RequestBody FriendReqDto request) {

		if(userService.isFriend(request.getUserIdCurrent(), request.getUserIdFriend())) {
			return ResponseEntity.ok().body("User was friend!");
		}
		userService.addFriend(request.getUserIdCurrent(), request.getUserIdFriend());
		
		return ResponseEntity.ok().body("Add friend successful!");
	}
	
	@DeleteMapping(value = { "" })
	public ResponseEntity<?> unFriend(@Valid @RequestBody FriendReqDto request) {

		if(!userService.isFriend(request.getUserIdCurrent(), request.getUserIdFriend())) {
			return ResponseEntity.ok().body("User was not friend!");
		}
		userService.unFriend(request.getUserIdCurrent(), request.getUserIdFriend());
		
		return ResponseEntity.ok().body("Unfriend successful!");
	}
}
