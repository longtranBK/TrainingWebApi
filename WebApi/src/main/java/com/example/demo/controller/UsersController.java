package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.UserInforInterface;
import com.example.demo.entity.User;
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
	
}
