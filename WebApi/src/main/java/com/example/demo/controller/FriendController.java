package com.example.demo.controller;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.request.FriendReqDto;
import com.example.demo.entity.UserFriend;
import com.example.demo.repository.UserFriendRepository;

@RestController
@RequestMapping("api/friends")
public class FriendController {
	
	@Autowired
	private UserFriendRepository userFriendRepository;
	
	@PostMapping(value = { "" })
	public ResponseEntity<?> addFriend(@Valid @RequestBody FriendReqDto request) {

		if(userFriendRepository.isFriend(request.getUserIdCurrent(), request.getUserIdFriend())) {
			return ResponseEntity.ok().body("User was friend!");
		}
		
		String id = UUID.randomUUID().toString();

		UserFriend userFriend = new UserFriend();
		userFriend.setId(id);
		userFriend.setUser1(request.getUserIdCurrent());
		userFriend.setUser2( request.getUserIdFriend());

		userFriendRepository.save(userFriend);

		return ResponseEntity.ok().body("Add friend successful!");

	}
	
	@DeleteMapping(value = { "" })
	public ResponseEntity<?> unFriend(@Valid @RequestBody FriendReqDto request) {

		if(!userFriendRepository.isFriend(request.getUserIdCurrent(), request.getUserIdFriend())) {
			return ResponseEntity.ok().body("User was not friend!");
		}
		
		userFriendRepository.unFriend(request.getUserIdCurrent(), request.getUserIdFriend());
		
		return ResponseEntity.ok().body("Unfriend successful!");

	}
}
