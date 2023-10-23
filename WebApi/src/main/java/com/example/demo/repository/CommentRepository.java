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
			+ " FROM comments WHERE post_id = ?1 AND delFlg = false ORDER BY createTs DESC"
			+ " LIMIT ?4,?3", nativeQuery = true)
	List<CommentCustomResDto> findByPostIdCustom(String postId, int limit, int offset);
	
	@Modifying
	@Query("update comments c set c.delFlg = ?2 where c.postId= ?1")
	void updateDelFlg(String postId, boolean delFlg);
	
	Comment findByCommentIdAndUserIdAndDelFlg(String commentId, String userId, boolean delFlg);
	
	@Query(value = "SELECT * FROM comment"
			+ " WHERE user_id = ?1 AND delFlg = false AND create_ts >= ?2 AND create_ts <= ?3"
			+ " ORDER BY create_ts DESC", nativeQuery = true)
	List<Comment> getCommentList(String userId, Date startDate, Date endDate);
	
	@Query(value = "SELECT count(user_id) FROM comments"
			+ " WHERE user_id = ?1 AND create_ts >= ?2 AND create_ts <= ?3", nativeQuery = true)
	int countCommentWithTime(String userId, Date startDate, Date endDate);
}
