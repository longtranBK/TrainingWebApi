package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.response.UserInforInterface;
import com.example.demo.entity.UserInfor;

@Repository
public interface UserInforRepository extends JpaRepository<UserInfor, String>{
	
	@Query(value = "SELECT * FROM user_infor WHERE id = ?1", nativeQuery = true)
	UserInfor findUserInforById(String id);
	
	@Query(value = "select "
			+ "u.id as id, "
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
			+ "where u.id = ?1", nativeQuery = true)
	UserInforInterface getUserInfor(String userId);
}
