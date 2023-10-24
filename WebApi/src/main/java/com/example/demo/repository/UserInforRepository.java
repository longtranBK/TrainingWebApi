package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.dto.response.UserInforResDto;
import com.example.demo.entity.UserInfor;

public interface UserInforRepository extends JpaRepository<UserInfor, String>{
	
	UserInfor findByUserId(String userId);
	
	@Query(value = "SELECT"
			+ "  u.id AS userId,"	
			+ "  ui.full_name AS fullName,"
			+ "  ai.image_url AS avatarUrl,"
			+ "  CASE "
			+ "    WHEN ui.sex = 0 THEN 'female'"
			+ "    WHEN ui.sex = 1 THEN 'male'"
			+ "    ELSE null"
			+ "  END AS sex,"
			+ "  ui.study_at AS studyAt,"
			+ "  ui.working_at AS workingAt,"
			+ "  ui.favorites AS favorites,"
			+ "  ui.other_infor As otherInfor,"
			+ "  ui.date_of_birth AS dateOfBirth"
			+ " FROM"
			+ "  users u INNER JOIN user_infor ui ON u.id = ui.id"
			+ "  LEFT JOIN avatar_image ai ON u.id = ai.user_id"
			+ " WHERE"
			+ "  u.id = ?2"
			+ "  AND u.id in (SELECT user2 as userId"
			+ "            FROM user_friend uf1"
			+ "            WHERE uf1.user1 = ?1"
			+ "            UNION"
			+ "            SELECT user1 as userId"
			+ "            FROM user_friend uf2"
			+ "            WHERE uf2.user2 = ?1)", nativeQuery = true)
	UserInforResDto getUserInfor(String userId, String userIdFriend);
	
	@Query(value = "SELECT"
			+ "  u.id AS userId,"			
			+ "  ui.full_name AS fullName,"
			+ "  ai.image_url AS avatarUrl,"
			+ "  CASE "
			+ "    WHEN ui.sex = 0 THEN 'female'"
			+ "    WHEN ui.sex = 1 THEN 'male'"
			+ "    ELSE null"
			+ "  END AS sex,"
			+ "  ui.study_at AS studyAt,"
			+ "  ui.working_at AS workingAt,"
			+ "  ui.favorites AS favorites,"
			+ "  ui.other_infor As otherInfor,"
			+ "  ui.date_of_birth AS dateOfBirth"
			+ " FROM"
			+ "  users u INNER JOIN user_infor ui ON u.id = ui.id"
			+ "  LEFT JOIN avatar_image ai ON u.id = ai.user_id"
			+ " WHERE"
			+ "  u.id = ?1", nativeQuery = true)
	UserInforResDto getUserInforMe(String userId);
}
