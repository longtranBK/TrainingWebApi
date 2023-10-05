package com.example.demo.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.Post;

public interface PostRepository extends JpaRepository<Post, String>{

	@Query(value = "SELECT * FROM posts WHERE user_id = ?1 AND create_ts >= ?2 AND create_ts <= ?3 ORDER BY create_ts DESC LIMIT ?4", nativeQuery = true)
	List<Post> getPostsCustom(String userId, Date startDate, Date endDate, int numbersPost);
	
	@Query(value = "SELECT * FROM posts"
			+ " WHERE user_id in (SELECT"
			+ " user2 as userid"
			+ " FROM user_friend"
			+ " WHERE user1 = ?1"
			+ " UNION"
			+ " SELECT"
			+ " user1 as userid"
			+ " FROM user_friend"
			+ " WHERE user2 = ?1)"
			+ " ORDER BY"
			+ " create_ts DESC"
			+ " LIMIT ?2", nativeQuery = true)
	List<Post> getPostTimeline(String userId, int numbersPost);
	
	Post findByPostIdAndUserId(String postId, String userId);
	
	Post findByPostId(String postId);
	
}
