package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.CommentLike;

public interface CommentLikeRepository  extends JpaRepository<CommentLike, String> {
	
	@Query(value = "SELECT count(1) FROM comment_likes cl inner join comments c on c.id = cl.comment_id"
			+ " WHERE c.id = ?1 and c.delFlg = false;", nativeQuery = true)
	int countLike(String commentId);
}
