package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.PostImage;

public interface PostImageRepository  extends JpaRepository<PostImage, String> {

	@Query(value = "SELECT image_url FROM post_image WHERE post_id = ?1", nativeQuery = true)
	List<String> findByPostId(String postId);
	
	@Transactional
	@Modifying
	void deleteByPostId(String postId);
}
