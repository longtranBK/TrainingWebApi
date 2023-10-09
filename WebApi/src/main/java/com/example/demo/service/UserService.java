package com.example.demo.service;

import java.text.ParseException;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.request.SignupReqDto;
import com.example.demo.dto.request.UpdateUserInforReqDto;
import com.example.demo.dto.response.UserInforResDto;
import com.example.demo.entity.User;

public interface UserService {

	/**
	 * Get user by id
	 * 
	 * @param userId
	 * @return User
	 */
	User getByUserId(String userId);
	
	/**
	 * Save user
	 * 
	 * @param req
	 * @param avatarFile
	 * @throws ParseException
	 */
	void saveUser(SignupReqDto req, MultipartFile avatarFile) throws ParseException;
	
	/**
	 * Set new password for user
	 * 
	 * @param user
	 * @param newPws
	 */
	void setNewPws(User user, String newPws);
	
	/**
	 * Get user infor
	 * 
	 * @param userId
	 * @return UserInforResDto
	 */
	UserInforResDto getUserInfor(String userId);
	
	/**
	 * Update user infor
	 * 
	 * @param user
	 * @param request
	 * @throws ParseException
	 */
	void updateUserInfor(User user, UpdateUserInforReqDto request, MultipartFile avatarFile) throws ParseException;
	
	/**
	 * Get user by username
	 * 
	 * @param username
	 * @return User
	 */
	User getByUsername(String username);
	
	/**
	 * Check friend of two user
	 * 
	 * @param userId1
	 * @param userId2
	 * @return true if had friend
	 */
	boolean isFriend(String userId1, String userId2);
	
	/**
	 * Add friend of two user
	 * 
	 * @param userId1
	 * @param userId2
	 */
	void addFriend(String userId1, String userId2);
	
	/**
	 * Unfriend of two user
	 * 
	 * @param userId1
	 * @param userId2
	 */
	void unFriend(String userId1, String userId2);
	
}
