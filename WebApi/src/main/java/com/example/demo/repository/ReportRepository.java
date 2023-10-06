package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Post;

@Repository
public interface ReportRepository {

	@Query(value = "SELECT * FROM posts WHERE user_id = ?1 AND create_ts >= ?2 AND create_ts <= ?3 ORDER BY create_ts DESC LIMIT ?4", nativeQuery = true)
	List<Post> getPostInfor();
}
