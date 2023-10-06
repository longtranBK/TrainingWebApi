package com.example.demo.service;

import java.sql.Date;
import java.text.ParseException;

import com.example.demo.dto.request.SignupReqDto;
import com.example.demo.dto.request.UpdateUserInforReqDto;
import com.example.demo.dto.response.UserInforInterface;
import com.example.demo.entity.User;

public interface UserService {

	User getByUserId(String userId);
	
	void saveUser(SignupReqDto req) throws ParseException;
	
	void setNewPws(User user, String newPws);
	
	UserInforInterface getUserInfor(String userId);
	
	void updateUserInfor(User user, UpdateUserInforReqDto request) throws ParseException;
	
	User getByUsername(String username);
	
	boolean isFriend(String userId1, String userId2);
	
	void addFriend(String userId1, String userId2);
	
	void unFriend(String userId1, String userId2);
	
	int numbersNewFriend(Date startDate, Date endDate);
}
