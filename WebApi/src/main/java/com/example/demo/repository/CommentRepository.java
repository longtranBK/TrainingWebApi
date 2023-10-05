package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.dto.response.CommentCustomInterface;
import com.example.demo.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, String> {

	@Query(value = "SELECT id AS commentId, user_id AS userId, content, create_ts AS createTs, update_ts AS updateTs FROM comment WHERE post_id = ?1 ORDER BY createTs DESC", nativeQuery = true)
	List<CommentCustomInterface> findByPostIdCustom(String postId);
	
	void deleteByPostId(String postId);
	
	void deleteByCommentId(String commentId);
	
	Comment findByCommentId(String commentId);
}
