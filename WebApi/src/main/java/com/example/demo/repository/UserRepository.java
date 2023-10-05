package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String>{

	User findByUserId(String userId);
	
	User findByUsername(String username);
	
	@Query(value = "SELECT * FROM users WHERE id = ?1", nativeQuery = true)
	User findUserById(String id);
    
    Boolean existsByEmail(String email);
    
    User findByUsernameAndEmail(String username, String email);
    
    User findByUsernameAndResetPasswordToken(String username, String resetPasswordToken);
	
	@Query(value = "SELECT is_active FROM user_infor WHERE id = (SELECT id FROM users WHERE username = ?1)", nativeQuery = true)
	boolean getActive(String username);
	
}
