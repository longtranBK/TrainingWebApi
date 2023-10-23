package com.example.demo.controller;

import java.text.ParseException;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.request.UpdatePasswordReqDto;
import com.example.demo.dto.request.UpdateUserInforReqDto;
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
	public ResponseEntity<?> getUserInfor(
			@PathVariable(value = "userId", required = true) @Size(max = 36) String userId) {

		String userIdCurrent = userService.getUserId();
		User user = userService.getByUserId(userId);
		if (user == null) {
			return ResponseEntity.ok().body("User not found!");
		}
		
		if (userId.equals(userIdCurrent)) {
			return ResponseEntity.ok().body("UserId have to not current user!");
		}
		
		UserInforResDto infor = userService.getUserInfor(userIdCurrent, userId);

		if (infor == null) {
			return ResponseEntity.ok().body("User is not friend!");
		}
		
		return ResponseEntity.ok().body(infor);
	}
	
	@Operation(summary = "Get user information of me")
	@GetMapping(value = "/me")
	public ResponseEntity<?> getUserCurrentInfor() {
		String userId = userService.getUserId();
		UserInforResDto infor = userService.getUserInforMe(userId);		
		return ResponseEntity.ok().body(infor);
	}

	@Operation(summary = "Update infor of me")
	@PutMapping(value = { "/me" })
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
