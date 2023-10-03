package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.Like;

public interface LikeRepository   extends JpaRepository<Like, String> {

	@Query(value = "SELECT user_id FROM likes WHERE post_id = ?1", nativeQuery = true)
	List<String> findByPostId(String postId);
}
