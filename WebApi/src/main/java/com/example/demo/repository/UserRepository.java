package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String>{

	User findByUsername(String username);
	
    Boolean existsByUsername(String username);
    
    Boolean existsByEmail(String email);
    
    User findByUsernameAndEmail(String username, String email);
    
    User findByUsernameAndEmailAndResetPasswordToken(String username, String email, String resetPasswordToken);
	
	@Query(value = "SELECT is_active FROM user_infor WHERE id = (SELECT id FROM users WHERE username = ?1)", nativeQuery = true)
	Boolean getActive(String username);
	
}
