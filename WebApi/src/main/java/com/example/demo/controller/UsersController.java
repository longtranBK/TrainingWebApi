package com.example.demo.controller;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.GetUserTimelineResDto;
import com.example.demo.dto.UpdateUserInforReqDto;
import com.example.demo.dto.UpdateUserInforResDto;
import com.example.demo.dto.UserInforInterface;
import com.example.demo.entity.User;
import com.example.demo.entity.UserInfor;
import com.example.demo.repository.UserInforRepository;
import com.example.demo.repository.UserRepository;

@RestController
@RequestMapping("api/user")
public class UsersController {

	@Autowired
	private UserRepository usersRepository;
	
	@Autowired
	private UserInforRepository usersInforRepository;
	
	@GetMapping("users")
	public List<User> getAllUsers() {
		return usersRepository.findAll();
	}
	
	@GetMapping(value = "getUserDetails")
	public ResponseEntity<UserInforInterface> getUserDetails(@RequestParam("username") String username) {
		User user = usersRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
		UserInforInterface infor = usersInforRepository.getUserInfor(username);
		
		return ResponseEntity.ok().body(infor);
	}
	
	@PostMapping(value = { "/updateUserInfor" })
	@Transactional(rollbackOn = {Exception.class, Throwable.class})
	public ResponseEntity<UpdateUserInforResDto> updateUserInfor(@Valid @RequestBody UpdateUserInforReqDto request) throws ParseException {
		UpdateUserInforResDto response = new UpdateUserInforResDto();
		Timestamp upadteTs = new java.sql.Timestamp(System.currentTimeMillis());
		
		User user = usersRepository.findUserById(request.getId());

		if(user == null) {
			throw new UsernameNotFoundException("User not found!");
		}

		user.setFullName(request.getFullName());
		user.setAvatarUrl(request.getAvatarUrl());
		user.setUpdateTs(upadteTs);
		usersRepository.save(user);
		
		UserInfor userInfor = usersInforRepository.findUserInforById(request.getId());
		userInfor.setSex(request.getSex());
		userInfor.setStudyAt(request.getStudyAt());
		userInfor.setWorkingAt(request.getWorkingAt());
		userInfor.setFavorites(request.getFavorites());
		userInfor.setOtherInfor(request.getOtherInfor());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date parsed = format.parse(request.getDateOfBirth());
		userInfor.setDateOfBirth(new java.sql.Date(parsed.getTime()));
		userInfor.setUpdateTs(upadteTs);
		
		usersInforRepository.save(userInfor);
		response.setMsg("User infor updated!");
		return ResponseEntity.ok().body(response);
		
	}
	
	@GetMapping(value = "getUserTimeline")
	public ResponseEntity<GetUserTimelineResDto> getUserTimeline(@RequestParam("userId") String userId) {
		User user = usersRepository.findUserById(userId);
		if (user == null) {
			throw new UsernameNotFoundException("User not found!");
		}
		GetUserTimelineResDto response = new GetUserTimelineResDto();
		response.setFullName(user.getFullName());
		response.setAvatarUrl(user.getAvatarUrl());
		
		return ResponseEntity.ok().body(response);
	}
	
}
