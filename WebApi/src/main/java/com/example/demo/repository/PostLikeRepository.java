package com.example.demo.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.PostLike;

public interface PostLikeRepository extends JpaRepository<PostLike, String> {

	@Query(value = "SELECT user_id FROM likes WHERE post_id = ?1", nativeQuery = true)
	List<String> findByPostId(String postId);
	
	@Transactional
	@Modifying
	void deleteByPostIdAndUserId(String postId, String userId);
	
	@Transactional
	@Modifying
	void deleteByPostId(String postId);
	
	@Query(value = "SELECT"
			+ " CASE"
			+ " WHEN COUNT(*) = 1 THEN 'true'"
			+ " ELSE 'false'"
			+ " END"
			+ " FROM likes"
			+ " WHERE user_id = ?1 AND post_id = ?2", nativeQuery = true)
	boolean hasLike(String userId, String postId);
	
	@Query(value = "SELECT count(*) FROM likes"
			+ " WHERE user_id =?1 AND create_ts >= ?2 AND create_ts <= ?3", nativeQuery = true)
	int countLike(String userId, Date startDate, Date endDate);
}
