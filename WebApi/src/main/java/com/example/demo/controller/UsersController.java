package com.example.demo.controller;

import java.text.ParseException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.constant.Constants;
import com.example.demo.dto.request.ResetPasswordReqDto;
import com.example.demo.dto.request.UpdateUserInforReqDto;
import com.example.demo.dto.response.GetUserTimelineResDto;
import com.example.demo.dto.response.UserInforResDto;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User", description = "API thao tác với user")
@RestController
@RequestMapping("api/users")
public class UsersController {

	@Autowired
	private UserService userService;

	@GetMapping(value = "/user-details/{userId}")
	@Secured(Constants.ROLE_USER_NAME)
	public ResponseEntity<?> getUserDetails(@PathVariable(value = "userId") String userId) {

		User user = userService.getByUserId(userId);
		if (user == null) {
			return ResponseEntity.ok().body("User not found!");
		}
		UserInforResDto infor = userService.getUserInfor(user.getUserId());

		return ResponseEntity.ok().body(infor);
	}

	@PostMapping(value = { "/user-details" })
	@Secured(Constants.ROLE_USER_NAME)
	public ResponseEntity<?> updateUserInfor(@Valid @RequestPart("request") UpdateUserInforReqDto request,
			@RequestPart("file") MultipartFile avatarFile) throws ParseException {

		User user = userService.getByUserId(userService.getUserId());
		if (userService.updateUserInfor(user, request, avatarFile) != null) {
			return ResponseEntity.ok().body("User details updated!");
		} else {
			return ResponseEntity.ok().body("User details error!");
		}
	}

	@GetMapping(value = "/timeline/{userId}")
	@Secured(Constants.ROLE_USER_NAME)
	public ResponseEntity<?> getUserTimeline(@PathVariable(value = "userId") String userId) {

		User user = userService.getByUserId(userId);
		if (user == null) {
			return ResponseEntity.ok().body("User not found!");
		}
		GetUserTimelineResDto response = new GetUserTimelineResDto();
		response.setUserId(userId);
		response.setFullName(user.getFullName());
		response.setAvatarUrl(user.getAvatarUrl());

		return ResponseEntity.ok().body(response);
	}

	@GetMapping(value = "/timeline/user-current")
	@Secured(Constants.ROLE_USER_NAME)
	public ResponseEntity<GetUserTimelineResDto> getCurrentUserTimeline() {

		User user = userService.getByUserId(userService.getUserId());

		GetUserTimelineResDto response = new GetUserTimelineResDto();
		response.setUserId(user.getUserId());
		response.setFullName(user.getFullName());
		response.setAvatarUrl(user.getAvatarUrl());

		return ResponseEntity.ok().body(response);
	}

	@PutMapping(value = { "/reset-password" })
	@Secured(Constants.ROLE_USER_NAME)
	public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordReqDto request) {
		
		User user = userService.getByUserId(userService.getUserId());
		if (userService.setNewPws(user, request.getNewPassword()) != null) {
			return ResponseEntity.ok().body("Password update succcessful!");
		} else {
			return ResponseEntity.ok().body("Password update error!");
		}
	}
}
