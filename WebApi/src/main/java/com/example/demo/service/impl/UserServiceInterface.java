package com.example.demo.service.impl;

import java.text.ParseException;

import com.example.demo.dto.request.SignupReqDto;
import com.example.demo.dto.request.UpdateUserInforReqDto;
import com.example.demo.dto.response.UserInforInterface;
import com.example.demo.entity.User;

public interface UserServiceInterface {

	User getByUserId(String userId);
	
	User getByUsernameAndResetTokenPws(String username, String tokenResetPws);
	
	void saveUser(SignupReqDto req) throws ParseException;
	
	String createTokenResetPws(User user);
	
	void setNewPws(User user, String newPws);
	
	UserInforInterface getUserInfor(String userId);
	
	void updateUserInfor(User user, UpdateUserInforReqDto request) throws ParseException;
	
	User getByUsername(String username);
}
