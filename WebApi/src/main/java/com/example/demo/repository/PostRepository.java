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
	
	@Query(value = "SELECT * FROM posts"
			+ " WHERE user_id in (SELECT"
			+ " user2 as userid"
			+ " FROM user_friend"
			+ " WHERE user1 = ?1"
			+ " UNION"
			+ " SELECT"
			+ " user1 as userid"
			+ " FROM user_friend"
			+ " WHERE user2 = ?1"
			+ " UNION"
			+ "	SELECT"
			+ "	?1 as userid)"
			+ " ORDER BY"
			+ " create_ts DESC"
			+ " LIMIT ?2", nativeQuery = true)
	List<Post> getPostTimeline(String userId, int numbersPost);
	
	Post findByPostIdAndUserIdAndDelFlg(String postId, String userId, boolean delFlg);
	
	Post findByPostId(String postId);
	
	@Modifying
	@Query("update posts p set p.delFlg = ?2 where p.id = ?1")
	void updateDelFlg(String postId, boolean delFlg);
	
}
