package com.example.demo.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.Post;

public interface PostRepository extends JpaRepository<Post, String>{

	@Query(value = "SELECT *"
			+ " FROM posts"
			+ " WHERE"
			+ "    user_id in (SELECT ?1 as userId"
			+ "			            UNION"
			+ "                SELECT user2 as userId"
			+ "			       FROM user_friend uf1"
			+ "			       WHERE uf1.user1 = ?1"
			+ "			       UNION"
			+ "			       SELECT user1 as userId"
			+ "			       FROM user_friend uf2"
			+ "			        WHERE uf2.user2 = ?1)"
			+ "    AND create_ts >= ?2"
			+ "    AND create_ts <= ?3"
			+ "    AND delFlg = false"
			+ "    ORDER BY create_ts DESC"
			+ "    LIMIT ?5,?4", nativeQuery = true)
	List<Post> getAllPosts(String userId, Date startDate, Date endDate, int limit, int offset);
	
	Post findByPostIdAndUserIdAndDelFlg(String postId, String userId, boolean delFlg);
	
	@Query(value = "SELECT *"
			+ " FROM posts"
			+ " WHERE"
			+ "    user_id in (SELECT ?1 as userId"
			+ "			            UNION"
			+ "                SELECT user2 as userId"
			+ "			       FROM user_friend uf1"
			+ "			       WHERE uf1.user1 = ?1"
			+ "			       UNION"
			+ "			       SELECT user1 as userId"
			+ "			       FROM user_friend uf2"
			+ "			        WHERE uf2.user2 = ?1)"
			+ "    AND delFlg = false"
			+ "    AND id = ?2", nativeQuery = true)
	Post findByPostIdOfmeOrFriend(String userId, String postId);
	
	@Modifying
	@Query("update posts p set p.delFlg = ?2 where p.id = ?1")
	void updateDelFlg(String postId, boolean delFlg);
	
	@Query(value = "SELECT *"
			+ " FROM posts"
			+ " WHERE"
			+ "    user_id in (SELECT ?1 as userId"
			+ "			            UNION"
			+ "                SELECT user2 as userId"
			+ "			       FROM user_friend uf1"
			+ "			       WHERE uf1.user1 = ?1"
			+ "			       UNION"
			+ "			       SELECT user1 as userId"
			+ "			       FROM user_friend uf2"
			+ "			        WHERE uf2.user2 = ?1)"
			+ "    AND delFlg = false"
			+ "    AND id = ?2"			
			+ "    LIMIT ?4,?3", nativeQuery = true)
	Post findPost(String userId, String postId, int limit, int offset);
	
}
