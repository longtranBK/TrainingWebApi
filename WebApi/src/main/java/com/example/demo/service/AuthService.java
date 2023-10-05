package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.repository.UserRepository;
import com.example.demo.service.impl.AuthServiceInterface;

@Service
public class AuthService implements AuthServiceInterface {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public boolean isActive(String username) {
		return userRepository.getActive(username);
	}

}
