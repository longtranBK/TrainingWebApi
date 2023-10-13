package com.example.demo.service;

import java.text.ParseException;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.request.SignupReqDto;
import com.example.demo.dto.request.UpdateUserInforReqDto;
import com.example.demo.dto.response.UserInforResDto;
import com.example.demo.entity.User;
import com.example.demo.entity.UserFriend;

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
	 * @return user if save success
	 * @throws ParseException if parse date fail
	 */
	User saveUser(SignupReqDto req, MultipartFile avatarFile) throws ParseException;
	
	/**
	 * Update new password user
	 * 
	 * @param user
	 * @param newPws
	 * @return User if update success
	 */
	User setNewPws(User user, String newPws);
	
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
	 * @param avatarFile
	 * @return User if update success
	 * @throws ParseException  if parse date fail
	 */
	User updateUserInfor(User user, UpdateUserInforReqDto request, MultipartFile avatarFile) throws ParseException;
	
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
	 * Add friend
	 * 
	 * @param userId1
	 * @param userId2
	 * @return UserFriend if add friend success
	 */
	UserFriend addFriend(String userId1, String userId2);
	
	/**
	 * Unfriend
	 * 
	 * @param userId1
	 * @param userId2
	 */
	void unFriend(String userId1, String userId2);
	
	/**
	 * Get user id of login user
	 * 
	 * @return userId
	 */
	String getUserId();
	
}
