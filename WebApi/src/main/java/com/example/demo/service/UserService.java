package com.example.demo.service;

import java.text.ParseException;

import com.example.demo.dto.request.SignupReqDto;
import com.example.demo.dto.request.UpdateUserInforReqDto;
import com.example.demo.dto.response.UserInforResDto;
import com.example.demo.entity.User;
import com.example.demo.entity.UserFriend;
import com.example.demo.entity.UserInfor;

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
	 * @return user if save success
	 * @throws ParseException if parse date fail
	 */
	User saveUser(SignupReqDto req) throws ParseException;
	
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
	 * @param userIdFriend
	 * @return UserInforResDto
	 */
	UserInforResDto getUserInfor(String userId, String userIdFriend);
	
	/**
	 * Get user infor me
	 * 
	 * @param userId
	 * @return GetUserInforResDto
	 */
	UserInforResDto getUserInforMe(String userId);
	
	/**
	 * Update user infor
	 * 
	 * @param request
	 * @return User if update success
	 * @throws ParseException  if parse date fail
	 */
	UserInfor updateUserInfor(UpdateUserInforReqDto request) throws ParseException;
	
	/**
	 * Get user by username
	 * 
	 * @param username
	 * @return User
	 */
	User findByUsername(String username);
	
	/**
	 * Check friend of two user
	 * 
	 * @param userId1
	 * @param userId2
	 * @return true if had friend
	 */
	boolean isFriend(String userId1, String userId2);
	
	/**
	 * Send request friend
	 * 
	 * @param userId1
	 * @param userId2
	 * @return UserFriend if add friend success
	 */
	UserFriend sentRequestFriend(String userId1, String userId2);
	
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
	
	/**
	 * Find user by username or email
	 * 
	 * @param username
	 * @return User
	 */
	User findByUsernameOrEmail(String username, String email);
	
	/**
	 * Find user by username and token
	 * 
	 * @param username
	 * @param token
	 * @return User
	 */
	User findByUsernameAndToken(String username, String token);
	
	/**
	 * Update password of me
	 * 
	 * @param currentPassword
	 * @param newPassword
	 * @return User if update success
	 */
	User updatePassword(String currentPassword, String newPassword);
	
	/**
	 * 
	 * @param userCurrentId
	 * @param userFriendId
	 * @return
	 */
	boolean hasSendRequest(String userCurrentId, String userFriendId);
	
	/**
	 * 
	 * @param userCurrentId
	 * @param userFriendId
	 * @return
	 */
	int cancelSendRequest(String userCurrentId, String userFriendId);
	
	/**
	 * 
	 * @param userCurrentId
	 * @param userFriendId
	 * @return
	 */
	UserFriend acceptFriend(String userCurrentId, String userFriendId);
	
}
