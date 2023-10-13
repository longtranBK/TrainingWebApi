package com.example.demo.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.constant.Constants;
import com.example.demo.dto.request.SignupReqDto;
import com.example.demo.dto.request.UpdateUserInforReqDto;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.entity.UserFriend;
import com.example.demo.entity.UserInfor;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserFriendRepository;
import com.example.demo.repository.UserInforRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.FileService;

@SpringBootTest
public class UserServiceImplTests {

	@MockBean
	private FileService fileService;

	@MockBean
	private RoleRepository roleRepository;

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private UserInforRepository userInforRepository;

	@MockBean
	private UserFriendRepository userFriendRepository;

	@Autowired
	private UserServiceImpl userServiceImpl;

	@Autowired
	WebApplicationContext webApplicationContext;

	@Test
	void getByUserId_Ok() throws Exception {
		when(userRepository.findByUserId(any(String.class))).thenReturn(new User());
		User user = userServiceImpl.getByUserId("Test");
		assertNotEquals(null, user);
	}

	@Test
	void getByUserName_Ok() throws Exception {
		when(userRepository.findByUsername(any(String.class))).thenReturn(new User());
		User user = userServiceImpl.getByUsername("Test");
		assertNotEquals(null, user);
	}

	@Test
	void saveUser_returnUser() throws Exception {
		SignupReqDto req = new SignupReqDto();
		req.setFullName("Test");
		req.setUsername("Test");
		req.setPassword("Test");
		req.setSex(0);
		req.setDateOfBirth("2020-01-01");
		Role role = new Role();
		role.setRoleId(Constants.ROLE_USER_VALUE);
		role.setRoleName(Constants.ROLE_USER_NAME);

		byte[] content = "test".getBytes();
		MultipartFile avatarFile = new MockMultipartFile("name", "file.txt", "text/plain", content);

		when(fileService.save(any(MultipartFile.class), any(String.class))).thenReturn("url");
		when(roleRepository.findByRoleName(Constants.ROLE_USER_NAME)).thenReturn(role);
		when(userRepository.save(any(User.class))).thenReturn(new User());
		when(userInforRepository.save(any(UserInfor.class))).thenReturn(new UserInfor());

		User usersave = userServiceImpl.saveUser(req, avatarFile);
		assertNotEquals(null, usersave);
	}

	@Test
	void saveUser_returnNull() throws Exception {
		SignupReqDto req = new SignupReqDto();
		req.setFullName("Test");
		req.setUsername("Test");
		req.setPassword("Test");
		req.setSex(0);
		req.setDateOfBirth("2020-01-01");
		Role role = new Role();
		role.setRoleId(Constants.ROLE_USER_VALUE);
		role.setRoleName(Constants.ROLE_USER_NAME);

		MultipartFile avatarFile = new MockMultipartFile("name", "file.txt", "text/plain", "test".getBytes());

		when(fileService.save(any(MultipartFile.class), any(String.class))).thenReturn("url");
		when(roleRepository.findByRoleName(Constants.ROLE_USER_NAME)).thenReturn(role);
		when(userRepository.save(any(User.class))).thenReturn(null);
		when(userInforRepository.save(any(UserInfor.class))).thenReturn(null);

		User usersave = userServiceImpl.saveUser(req, avatarFile);
		assertEquals(null, usersave);
	}

	@Test
	void setNewPws_Ok() throws Exception {
		User user = new User();
		when(userRepository.save(any(User.class))).thenReturn(new User());
		User usersave = userServiceImpl.setNewPws(user, "pws");
		assertNotEquals(null, usersave);
	}

	@Test
	void getUserInfor_Ok() throws Exception{
		when(userInforRepository.getUserInfor(any(String.class))).thenReturn(null);
		userServiceImpl.getUserInfor("Test");
	}

	@Test
	void updateUserInfor_hadDateOfBirth_saveOk() throws Exception {
		User user = new User();
		UpdateUserInforReqDto request = new UpdateUserInforReqDto();
		request.setFullName("test");
		request.setSex(0);
		request.setFavorites("test");
		request.setStudyAt("test");
		request.setWorkingAt("test");
		request.setDateOfBirth("2000-01-01");
		request.setOtherInfor("test");
		MultipartFile avatarFile = new MockMultipartFile("name", "file.txt", "text/plain", "test".getBytes());

		when(userRepository.save(any(User.class))).thenReturn(new User());
		when(userInforRepository.findUserInforById(user.getUserId())).thenReturn(new UserInfor());
		when(userInforRepository.save(any(UserInfor.class))).thenReturn(new UserInfor());

		User usersave = userServiceImpl.updateUserInfor(user, request, avatarFile);
		assertNotEquals(null, usersave);
	}

	@Test
	void updateUserInfor_noDateOfBirth_saveOk() throws Exception {
		User user = new User();
		UpdateUserInforReqDto request = new UpdateUserInforReqDto();
		request.setFullName("test");
		request.setSex(0);
		request.setFavorites("test");
		request.setStudyAt("test");
		request.setWorkingAt("test");
		request.setOtherInfor("test");
		MultipartFile avatarFile = new MockMultipartFile("name", "file.txt", "text/plain", "test".getBytes());

		when(userRepository.save(any(User.class))).thenReturn(new User());
		when(userInforRepository.findUserInforById(user.getUserId())).thenReturn(new UserInfor());
		when(userInforRepository.save(any(UserInfor.class))).thenReturn(new UserInfor());

		User usersave = userServiceImpl.updateUserInfor(user, request, avatarFile);
		assertNotEquals(null, usersave);
	}

	@Test
	void updateUserInfor_noDateOfBirth_saveNg() throws Exception {
		User user = new User();
		UpdateUserInforReqDto request = new UpdateUserInforReqDto();
		request.setFullName("test");
		request.setSex(0);
		request.setFavorites("test");
		request.setStudyAt("test");
		request.setWorkingAt("test");
		request.setOtherInfor("test");
		MultipartFile avatarFile = new MockMultipartFile("name", "file.txt", "text/plain", "test".getBytes());

		when(userRepository.save(any(User.class))).thenReturn(null);
		when(userInforRepository.findUserInforById(user.getUserId())).thenReturn(new UserInfor());
		when(userInforRepository.save(any(UserInfor.class))).thenReturn(null);

		User usersave = userServiceImpl.updateUserInfor(user, request, avatarFile);
		assertEquals(null, usersave);
	}

	@Test
	void isFriend_Ok() throws Exception{
		when(userFriendRepository.isFriend(any(String.class), any(String.class))).thenReturn(true);
		boolean isFriend = userServiceImpl.isFriend("user1", "user2");
		assertTrue(isFriend);
	}

	@Test
	void addFriend_Ok() throws Exception{
		when(userFriendRepository.save(any(UserFriend.class))).thenReturn(new UserFriend());
		UserFriend relation = userServiceImpl.addFriend("user1", "user2");
		assertNotEquals(null, relation);
	}

	@Test
	void unFriend_Ok() throws Exception {
		doNothing().when(userFriendRepository).unFriend(any(String.class), any(String.class));
		userServiceImpl.unFriend("user1", "user2");
	}

	@Test
	void getUserId_returnUserId() throws Exception {
		Set<GrantedAuthority> authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority(Constants.ROLE_USER_NAME));
		UsernamePasswordAuthenticationToken userTest = new UsernamePasswordAuthenticationToken("test", "test",
				authorities);
		SecurityContextHolder.getContext().setAuthentication(userTest);

		User user = new User();
		user.setUserId("userId");
		when(userRepository.findByUsername(any(String.class))).thenReturn(user);
		String userId = userServiceImpl.getUserId();
		assertEquals("userId", userId);
	}

}
