package com.example.demo.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.constant.Constants;
import com.example.demo.constant.RoleEnum;
import com.example.demo.constant.StatusFriendEnum;
import com.example.demo.dto.request.SignupReqDto;
import com.example.demo.dto.request.UpdateUserInforReqDto;
import com.example.demo.dto.response.UserInforResDto;
import com.example.demo.entity.AvatarImage;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.entity.UserFriend;
import com.example.demo.entity.UserInfor;
import com.example.demo.repository.AvatarImageRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserFriendRepository;
import com.example.demo.repository.UserInforRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserInforRepository userInforRepository;

	@Autowired
	private UserFriendRepository userFriendRepository;

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private AvatarImageRepository avatarImageRepository;
	
	@Override
	public User getByUserId(String userId) {
		return userRepository.findByUserId(userId);
	}

	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	@Transactional(rollbackOn = { Exception.class, Throwable.class })
	public User saveUser(SignupReqDto request) throws ParseException {
		String uuid = UUID.randomUUID().toString();
		User user = new User();

		user.setUserId(uuid);
		user.setUsername(request.getUsername());
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));

		Role role = roleRepository.findByRoleName(RoleEnum.USER.getLabel());
		user.addRole(role);

		User userSave = userRepository.save(user);

		UserInfor userInfor = new UserInfor();
		userInfor.setUserId(uuid);
		userInfor.setIsActive(Constants.ACTIVE_USER);

		if (userSave != null && userInforRepository.save(userInfor) != null) {
			return userSave;
		}
		return null;

	}

	@Override
	public User setNewPws(User user, String newPws) {
		user.setResetPasswordToken(null);
		user.setPassword(passwordEncoder.encode(newPws));
		return userRepository.save(user);
	}

	@Override
	public UserInforResDto getUserInfor(String userId, String userIdFriend) {
		return userInforRepository.getUserInfor(userId, userIdFriend);
	}

	@Override
	@Transactional(rollbackOn = { Exception.class, Throwable.class })
	public UserInfor updateUserInfor(UpdateUserInforReqDto request) throws ParseException {
		String userId = getUserId();
		UserInfor infor = userInforRepository.findByUserId(userId);
		infor.setFullName(request.getFullName());
		infor.setSex(request.getSex());
		infor.setStudyAt(request.getStudyAt());
		infor.setWorkingAt(request.getWorkingAt());
		infor.setFavorites(request.getFavorites());
		infor.setOtherInfor(request.getOtherInfor());
		SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT);
		Date parsed = format.parse(request.getDateOfBirth());
		infor.setDateOfBirth(new java.sql.Date(parsed.getTime()));
		infor.setAvatarUrl(request.getAvatarUrl());
		
		AvatarImage avatar = new AvatarImage();
		avatar.setUserId(userId);
		avatar.setImageUrl(request.getAvatarUrl());
		avatarImageRepository.save(avatar);
		infor.setAvatarUrl(avatar.getImageUrl());
		
		return userInforRepository.save(infor);
	}

	@Override
	public boolean isFriend(String userId1, String userId2) {
		return userFriendRepository.isFriend(userId1, userId2);
	}

	@Override
	public UserFriend sentRequestFriend(String userId1, String userId2) {
		UserFriend userFriend = new UserFriend();
		userFriend.setUser1(userId1);
		userFriend.setUser2(userId2);
		userFriend.setStatus(StatusFriendEnum.REQUEST.getValue());
		return userFriendRepository.save(userFriend);
	}

	@Override
	public void unFriend(String userId1, String userId2) {
		userFriendRepository.unFriend(userId1, userId2);
	}

	@Override
	public String getUserId() {

		SecurityContext securityContext = SecurityContextHolder.getContext();
		String username = securityContext.getAuthentication().getName();
		User user = userRepository.findByUsername(username);

		return user.getUserId();
	}

	@Override
	public List<User> findByUsernameOrEmail(String username, String email) {
		return userRepository.findByUsernameOrEmail(username, email);
	}

	@Override
	public User findByUsernameAndToken(String username, String token) {
		return userRepository.findByUsernameAndResetPasswordToken(username, token);
	}

	@Override
	public UserInforResDto getUserInforMe(String userId) {
		return userInforRepository.getUserInforMe(userId);
	}

	@Override
	public User updatePassword(String currentPassword, String newPassword) {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		String username = securityContext.getAuthentication().getName();
		try {
			Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(username, currentPassword));
		} catch (BadCredentialsException e) {
			return null;
		}
		User user = userRepository.findByUsername(username);

		user.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(user);
		return userRepository.save(user);
	}

	@Override
	public boolean hasSendRequestToU1OrU2(String userCurrentId, String userFriendId) {
		return userFriendRepository.hasSendRequestFriendToU1OrU2(userCurrentId, userFriendId);
	}

	@Override
	public int cancelSendRequest(String userCurrentId, String userFriendId) {
		return userFriendRepository.cancelRequestFriend(userCurrentId, userFriendId);
	}

	@Override
	public UserFriend acceptFriend(String userCurrentId, String userFriendId) {
		UserFriend userFriend = userFriendRepository.findByUser1AndUser2AndStatus(userFriendId, userCurrentId, StatusFriendEnum.REQUEST.getValue());
		userFriend.setStatus(StatusFriendEnum.FRIEND.getValue());
		return userFriendRepository.save(userFriend);
	}

	@Override
	public User updateResetPasswordToken(String token, User user) {
		user.setResetPasswordToken(token);
		return userRepository.save(user);
	}

	@Override
	public boolean hasReceiveRequestToU2(String userCurrentId, String userFriendId) {
		return userFriendRepository.hasReceiveRequestFriendToU2(userCurrentId, userFriendId);
	}

}
