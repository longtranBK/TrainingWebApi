package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.demo.entity.UserFriend;

@Repository
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
}
