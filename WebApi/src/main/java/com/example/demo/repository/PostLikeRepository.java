package com.example.demo.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.PostLike;

public interface PostLikeRepository extends JpaRepository<PostLike, String> {

	@Query(value = "SELECT user_id FROM likes WHERE post_id = ?1", nativeQuery = true)
	List<String> findByPostId(String postId);
	
	@Query(value = "SELECT"
			+ " CASE"
			+ " WHEN COUNT(user_id) = 1 THEN 'true'"
			+ " ELSE 'false'"
			+ " END"
			+ " FROM post_likes"
			+ " WHERE user_id = ?1 AND post_id = ?2", nativeQuery = true)
	boolean hasLike(String userId, String postId);
	
	@Query(value = "SELECT count(user_id) FROM post_likes"
			+ " WHERE user_id =?1 AND create_ts >= ?2 AND create_ts <= ?3", nativeQuery = true)
	int countLike(String userId, Date startDate, Date endDate);
	
	@Query(value = "SELECT count(user_id) FROM post_likes WHERE post_id = ?1", nativeQuery = true)
	int countTotalLike(String postId);
}
