package com.example.demo.controller;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.request.UpdateUserInforReqDto;
import com.example.demo.dto.response.GetUserTimelineResDto;
import com.example.demo.dto.response.UserInforInterface;
import com.example.demo.entity.User;
import com.example.demo.entity.UserInfor;
import com.example.demo.repository.UserInforRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("api/users")
public class UsersController {

	@Autowired
	private UserService userService;
	
	@GetMapping(value = "/user-details/{userId}")
	public ResponseEntity<UserInforInterface> getUserDetails(@PathVariable(value = "userId") String userId) {
		User user = userService.getByUserId(userId);
		
		if (user == null) {
			throw new UsernameNotFoundException("User not found!");
		}
		UserInforInterface infor = userService.getUserInfor(user.getUserId());
		return ResponseEntity.ok().body(infor);
	}

	@PutMapping(value = { "/user-details/{userId}" })
	public ResponseEntity<?> updateUserInfor(@PathVariable(value = "userId") String userId,
			@Valid @RequestBody UpdateUserInforReqDto request) throws ParseException {
		
		User user = userService.getByUserId(userId);
		if (user == null) {
			return ResponseEntity.notFound().build();
		}
		userService.updateUserInfor(user, request);
		return ResponseEntity.ok().body("User details updated!");

	}

	@GetMapping(value = "/timeline/{userId}")
	public ResponseEntity<GetUserTimelineResDto> getUserTimeline(@PathVariable(value = "userId") String userId) {
		User user = userService.getByUserId(userId);
		if (user == null) {
			throw new UsernameNotFoundException("User not found!");
		}
		GetUserTimelineResDto response = new GetUserTimelineResDto();
		response.setFullName(user.getFullName());
		response.setAvatarUrl(user.getAvatarUrl());

		return ResponseEntity.ok().body(response);
	}

	@GetMapping(value = "/timeline/user-current")
	public ResponseEntity<GetUserTimelineResDto> getCurrentUserTimeline() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		String username = securityContext.getAuthentication().getName();
		User user = userService.getByUsername(username);

		GetUserTimelineResDto response = new GetUserTimelineResDto();
		response.setFullName(user.getFullName());
		response.setAvatarUrl(user.getAvatarUrl());

		return ResponseEntity.ok().body(response);
	}

}
