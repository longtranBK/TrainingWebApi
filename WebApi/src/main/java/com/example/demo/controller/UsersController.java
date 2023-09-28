package com.example.demo.controller;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

import com.example.demo.dto.SignupRequestDto;
import com.example.demo.dto.SignupResponseDto;
import com.example.demo.dto.UpdateUserInforRequestDto;
import com.example.demo.dto.UpdateUserInforResponseDto;
import com.example.demo.dto.UserInforInterface;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.entity.UserInfor;
import com.example.demo.repository.UserInforRepository;
import com.example.demo.repository.UserRepository;

@RestController
@RequestMapping("api")
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
	public ResponseEntity<UpdateUserInforResponseDto> updateUserInfor(@Valid @RequestBody UpdateUserInforRequestDto updateUserInforRequestDto) throws ParseException {
		UpdateUserInforResponseDto updateUserInforResponseDto = new UpdateUserInforResponseDto();
		Timestamp upadteTs = new java.sql.Timestamp(System.currentTimeMillis());
		
		User user = usersRepository.findUserById(updateUserInforRequestDto.getId());

		if(user == null) {
			throw new UsernameNotFoundException("User not found!");
		}

		user.setFullName(updateUserInforRequestDto.getFullName());
		user.setAvatarUrl(updateUserInforRequestDto.getAvatarUrl());
		user.setUpdateTs(upadteTs);
		usersRepository.save(user);
		
		UserInfor userInfor = usersInforRepository.findUserInforById(updateUserInforRequestDto.getId());
		userInfor.setSex(updateUserInforRequestDto.getSex());
		userInfor.setStudyAt(updateUserInforRequestDto.getStudyAt());
		userInfor.setWorkingAt(updateUserInforRequestDto.getWorkingAt());
		userInfor.setFavorites(updateUserInforRequestDto.getFavorites());
		userInfor.setOtherInfor(updateUserInforRequestDto.getOtherInfor());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date parsed = format.parse(updateUserInforRequestDto.getDateOfBirth());
		userInfor.setDateOfBirth(new java.sql.Date(parsed.getTime()));
		userInfor.setUpdateTs(upadteTs);
		
		usersInforRepository.save(userInfor);
		updateUserInforResponseDto.setMsg("User infor updated!");
		return ResponseEntity.ok().body(updateUserInforResponseDto);
		
	}
	
}
