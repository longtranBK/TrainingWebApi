package com.example.demo.service.impl;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.demo.constant.Constants;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

@SpringBootTest
public class UserDetailsServiceImplTests {

	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;

	@MockBean
	private UserRepository userRepository;

	@Test
	void loadUserByUsername_Ng() throws Exception{
		when(userRepository.findByUsername(any(String.class))).thenReturn(null);
		
		assertThrows(UsernameNotFoundException.class,
	            ()->{
	            	userDetailsServiceImpl.loadUserByUsername("Test");
	            });
	}

	@Test
	void loadUserByUsername_Ok() throws Exception {
		User user = new User();
		user.setUsername("Test");
		user.setPassword("Test");
		Role role = new Role();
		role.setRoleId(Constants.ROLE_USER_VALUE);
		role.setRoleName(Constants.ROLE_USER_NAME);
		Set<Role> roleList = new HashSet<>();
		roleList.add(role);
		user.setRoles(roleList);
		when(userRepository.findByUsername(any(String.class))).thenReturn(user);

		UserDetails userdetails = userDetailsServiceImpl.loadUserByUsername("Test");

		assertNotEquals(null, userdetails);
	}
}
