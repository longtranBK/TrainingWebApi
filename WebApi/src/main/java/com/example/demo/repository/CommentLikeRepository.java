package com.example.demo.repository;

import java.sql.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.CommentLike;

public interface CommentLikeRepository  extends JpaRepository<CommentLike, String> {
	
	@Query(value = "SELECT count(1) FROM comment_likes cl inner join comments c on c.id = cl.comment_id"
			+ " WHERE c.id = ?1 and c.delFlg = false;", nativeQuery = true)
	int countLike(String commentId);
	
	@Query(value = "SELECT"
			+ " CASE"
			+ " WHEN COUNT(user_id) = 1 THEN 'true'"
			+ " ELSE 'false'"
			+ " END"
			+ " FROM comment_likes"
			+ " WHERE user_id = ?1 AND comment_id = ?2", nativeQuery = true)
	boolean hasLike(String userId, String commentId);
	
	CommentLike findByCommentIdAndUserId(String commentId, String postId);
	
	@Query(value = "SELECT count(user_id) FROM comment_likes"
			+ " WHERE user_id =?1 AND create_ts >= ?2 AND create_ts <= ?3", nativeQuery = true)
	int countLikeWithTime(String userId, Date startDate, Date endDate);
}
