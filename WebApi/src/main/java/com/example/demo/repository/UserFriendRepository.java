package com.example.demo.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.UserFriend;

public interface UserFriendRepository  extends JpaRepository<UserFriend, String>{

	@Query(value = "SELECT"
			+ " CASE"
			+ "    WHEN count(*) = 1 THEN TRUE"
			+ "    ELSE FALSE"
			+ " END"
			+ " FROM user_friend"
			+ " WHERE (user1 = ?1 AND user2 = ?2)"
			+ " OR (user2 = ?1 AND user1 = ?2);", nativeQuery = true)
	Boolean isFriend(String userId1, String userId2);
	
	@Query(value = "DELETE"
			+ " FROM user_friend"
			+ " WHERE (user1 = ?1 AND user2 = ?2)"
			+ " OR (user2 = ?1 AND user1 = ?2);", nativeQuery = true)
	void unFriend(String userId1, String userId2);
	
	@Query(value = "SELECT user2 as userId"
			+ " FROM user_friend uf1"
			+ " WHERE uf1.user1 = ?1 AND create_ts >= ?2 AND create_ts <= ?3"
			+ " UNION"
			+ " SELECT user1 as userId"
			+ " FROM user_friend uf2"
			+ " WHERE uf2.user2 = ?1 AND create_ts >= ?2 AND create_ts <= ?3", nativeQuery = true)
	List<String> getUserIdFriendList(String userId, Date startTime, Date endTime);
}
