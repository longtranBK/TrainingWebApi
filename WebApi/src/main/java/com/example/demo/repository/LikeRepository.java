package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Like;

public interface LikeRepository extends JpaRepository<Like, String> {

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
}
