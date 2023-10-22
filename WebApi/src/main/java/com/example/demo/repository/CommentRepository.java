package com.example.demo.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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
	
	@Modifying
	@Query("update comments c set c.delFlg = ?2 where c.postId= ?1")
	void updateDelFlg(String postId, boolean delFlg);
	
	Comment findByCommentIdAndUserId(String commentId, String userId);
	
	@Query(value = "SELECT * FROM comment"
			+ " WHERE user_id = ?1 AND create_ts >= ?2 AND create_ts <= ?3"
			+ " ORDER BY create_ts DESC", nativeQuery = true)
	List<Comment> getCommentList(String userId, Date startDate, Date endDate);
}
