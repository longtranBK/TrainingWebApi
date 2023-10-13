package com.example.demo.service.impl;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.constant.Constants;
import com.example.demo.dto.request.SignupReqDto;
import com.example.demo.dto.request.UpdateUserInforReqDto;
import com.example.demo.dto.response.UserInforResDto;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.entity.UserFriend;
import com.example.demo.entity.UserInfor;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserFriendRepository;
import com.example.demo.repository.UserInforRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.FileService;
import com.example.demo.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private FileService fileService;

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

	@Override
	public User getByUserId(String userId) {
		return userRepository.findByUserId(userId);
	}

	@Override
	public User getByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	@Transactional(rollbackOn = { Exception.class, Throwable.class })
	public User saveUser(SignupReqDto request, MultipartFile avatarFile) throws ParseException {
		Timestamp upadteTs = new java.sql.Timestamp(System.currentTimeMillis());
		String uuid = UUID.randomUUID().toString();
		User user = new User();

		user.setUserId(uuid);
		user.setFullName(request.getFullName());
		user.setAvatarUrl(fileService.save(avatarFile, uuid + "/"));
		user.setUsername(request.getUsername());
		user.setPassword(passwordEncoder.encode(request.getPassword()));

		Role role = roleRepository.findByRoleName(Constants.ROLE_USER_NAME);
		user.addRole(role);

		user.setCreateTs(upadteTs);
		user.setUpdateTs(upadteTs);

		User userSave = userRepository.save(user);

		UserInfor userInfor = new UserInfor();
		userInfor.setUserId(uuid);
		userInfor.setIsActive(1);
		userInfor.setSex(request.getSex());
		SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT);
		Date parsed = format.parse(request.getDateOfBirth());
		userInfor.setDateOfBirth(new java.sql.Date(parsed.getTime()));
		userInfor.setCreateTs(upadteTs);
		userInfor.setUpdateTs(upadteTs);

		if (userSave != null && userInforRepository.save(userInfor)!= null) {
			return userSave;
		}
		return null;

	}

	@Override
	public User setNewPws(User user, String newPws) {
		user.setPassword(passwordEncoder.encode(newPws));
		return userRepository.save(user);
	}

	@Override
	public UserInforResDto getUserInfor(String userId) {
		return userInforRepository.getUserInfor(userId);
	}

	@Override
	@Transactional(rollbackOn = { Exception.class, Throwable.class })
	public User updateUserInfor(User user, UpdateUserInforReqDto request, MultipartFile avatarFile)
			throws ParseException {
		Timestamp upadteTs = new java.sql.Timestamp(System.currentTimeMillis());
		user.setFullName(request.getFullName());
		user.setAvatarUrl(fileService.save(avatarFile, user.getUserId() + "/"));
		user.setUpdateTs(upadteTs);
		User userSave = userRepository.save(user);

		UserInfor userInfor = userInforRepository.findUserInforById(user.getUserId());
		userInfor.setSex(request.getSex());
		userInfor.setStudyAt(request.getStudyAt());
		userInfor.setWorkingAt(request.getWorkingAt());
		userInfor.setFavorites(request.getFavorites());
		userInfor.setOtherInfor(request.getOtherInfor());
		if (request.getDateOfBirth() != null) {
			SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT);
			Date parsed = format.parse(request.getDateOfBirth());
			userInfor.setDateOfBirth(new java.sql.Date(parsed.getTime()));
		}
		userInfor.setUpdateTs(upadteTs);

		if (userSave != null && userInforRepository.save(userInfor) != null) {
			return userSave;
		}
		
		return null;
	}

	@Override
	public boolean isFriend(String userId1, String userId2) {
		return userFriendRepository.isFriend(userId1, userId2);
	}

	@Override
	public UserFriend addFriend(String userId1, String userId2) {

		Timestamp createTs = new java.sql.Timestamp(System.currentTimeMillis());
		UserFriend userFriend = new UserFriend();
		userFriend.setUser1(userId1);
		userFriend.setUser2(userId2);
		userFriend.setCreateTs(createTs);

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

}
