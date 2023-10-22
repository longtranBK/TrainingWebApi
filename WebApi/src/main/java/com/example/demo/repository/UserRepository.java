package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.User;

public interface UserRepository extends JpaRepository<User, String>{

	User findByUserId(String userId);
	
	User findByUserIdAndPassword(String userId, String password);
	
	User findByUsername(String username);
    
	@Query(value = "SELECT is_active FROM user_infor WHERE id = (SELECT id FROM users WHERE username = ?1)", nativeQuery = true)
	boolean getActive(String username);
	
	User findByUsernameOrEmail(String username, String email);
	
	User findByUsernameAndResetPasswordToken(String username, String resetPasswordToken);
	
}
