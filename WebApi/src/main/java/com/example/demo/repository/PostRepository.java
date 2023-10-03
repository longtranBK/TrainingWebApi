package com.example.demo.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.Post;

public interface PostRepository extends JpaRepository<Post, String>{

	@Query(value = "SELECT * FROM posts WHERE user_id = ?1 AND create_ts >= ?2 AND create_ts <= ?3 ORDER BY create_ts DESC LIMIT ?4", nativeQuery = true)
	List<Post> getAllPostCustom(String userId, Date startDate, Date endDate, int numbersPost);
}
