package com.example.demo.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.dto.response.CommentCustomResDto;
import com.example.demo.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, String> {

	@Query(value = "SELECT"
			+ " comment_id AS commentId,"
			+ " user_id AS userId,"
			+ " content,"
			+ " create_ts AS createTs,"
			+ " update_ts AS updateTs"
			+ " FROM comment WHERE post_id = ?1 ORDER BY createTs DESC", nativeQuery = true)
	List<CommentCustomResDto> findByPostIdCustom(String postId);
	
	void deleteByPostId(String postId);
	
	Comment findByCommentIdAndUserId(String commentId, String userId);
	
	@Query(value = "SELECT * FROM comment"
			+ " WHERE create_ts >= ?1 AND create_ts <= ?2"
			+ " ORDER BY create_ts DESC", nativeQuery = true)
	List<Comment> getCommentList(Date startDate, Date endDate);
}
