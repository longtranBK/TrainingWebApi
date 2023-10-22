package com.example.demo.controller;

import java.text.ParseException;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.constant.Constants;
import com.example.demo.dto.request.ResetPasswordReqDto;
import com.example.demo.dto.request.UpdatePasswordReqDto;
import com.example.demo.dto.request.UpdateUserInforReqDto;
import com.example.demo.dto.response.GetUserTimelineResDto;
import com.example.demo.dto.response.UserInforResDto;
import com.example.demo.entity.User;
import com.example.demo.entity.UserInfor;
import com.example.demo.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User", description = "API thao tác với user")
@Validated
@RestController
@RequestMapping("api/user-infor")
public class UsersController {

	@Autowired
	private UserService userService;

	@Operation(summary = "Get user information of friend with userIdFriend")
	@GetMapping(value = "/{userId}")
	@Secured(Constants.ROLE_USER_NAME)
	public ResponseEntity<?> getUserInfor(
			@PathVariable(value = "userId", required = true) @Size(max = 36) String userId) {

		User user = userService.getByUserId(userId);
		if (user == null) {
			return ResponseEntity.ok().body("User not found!");
		}
		UserInforResDto infor = userService.getUserInfor(user.getUserId(), userId);

		if (infor == null) {
			return ResponseEntity.ok().body("User is not friend!");
		}
		
		return ResponseEntity.ok().body(infor);
	}
	
	@Operation(summary = "Get user information of me")
	@GetMapping(value = "/me")
	@Secured(Constants.ROLE_USER_NAME)
	public ResponseEntity<?> getUserCurrentInfor() {
		String userId = userService.getUserId();
		UserInforResDto infor = userService.getUserInforMe(userId);		
		return ResponseEntity.ok().body(infor);
	}

	@Operation(summary = "Update infor of me")
	@PutMapping(value = { "/me" })
	@Secured(Constants.ROLE_USER_NAME)
	public ResponseEntity<?> updateUserCurrentInfor(
			@Valid @RequestBody(required = true) UpdateUserInforReqDto request) throws ParseException {

		UserInfor infor = userService.updateUserInfor(request);
		if (infor != null) {
			return ResponseEntity.ok().body(infor);
		} else {
			return ResponseEntity.internalServerError().body("User details error!");
		}
	}
	
	@Operation(summary = "Update password of me")
	@PutMapping(value = { "/me/password" })
	@Secured(Constants.ROLE_USER_NAME)
	public ResponseEntity<?> updatePassword(
			@Valid @RequestBody(required = true) UpdatePasswordReqDto request) throws ParseException {

		User user = userService.updatePassword(request.getCurrentPassword(), request.getNewPassword());
		if (user != null) {
			return ResponseEntity.ok().body("Update password successful!");
		} else {
			return ResponseEntity.internalServerError().body("Current password is incorrect!");
		}
	}

}
