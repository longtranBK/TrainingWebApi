package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.UserInforInterface;
import com.example.demo.entity.UserInfor;

@Repository
public interface UserInforRepository extends JpaRepository<UserInfor, String>{

	Optional<UserInfor> findById(String id);
	
	@Query(value = "select "
			+ "u.email as email, "
			+ "u.full_name as fullName, "
			+ "u.avatar_url as avatarUrl, "
			+ "ui.sex as sex, "
			+ "ui.study_at as studyAt, "
			+ "ui.working_at as workingAt, "
			+ "ui.favorites as favorites, "
			+ "ui.other_infor as otherInfor, "
			+ "ui.date_of_birth as dateOfBirth "
			+ "from users u inner join user_infor ui "
			+ "on u.id = ui.id "
			+ "where u.username = ?1", nativeQuery = true)
	UserInforInterface getUserInfor(String username);
}