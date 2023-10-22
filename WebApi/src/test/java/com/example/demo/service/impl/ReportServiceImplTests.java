package com.example.demo.service.impl;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.example.demo.entity.Comment;
import com.example.demo.entity.Post;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserFriendRepository;

@SpringBootTest
public class ReportServiceImplTests {

	@MockBean
	private CommentRepository commentRepository;

	@MockBean
	private UserFriendRepository userFriendRepository;

	@MockBean
	private PostRepository postRepository;
	
//	@MockBean
//	private LikeRepository likeRepository;
	
	@Autowired
	private ReportServiceImpl reportServiceImpl;

	@Test
	void loadData_Ok() throws Exception {
		List<Post> postList = new ArrayList<>();
		List<String> userIdFriendList = new ArrayList<>();
		List<Comment> commentList = new ArrayList<>();	
		
//		when(postRepository.getPostsCustom(any(String.class), any(java.sql.Date.class), any(java.sql.Date.class), any(int.class))).thenReturn(postList);
		when(userFriendRepository.getUserIdFriendList(any(String.class), any(java.sql.Date.class), any(java.sql.Date.class))).thenReturn(userIdFriendList);
//		when(likeRepository.countLike(any(String.class), any(java.sql.Date.class), any(java.sql.Date.class))).thenReturn(5);
		when(commentRepository.getCommentList(any(String.class), any(java.sql.Date.class), any(java.sql.Date.class))).thenReturn(commentList);
		
//		ByteArrayInputStream data = reportServiceImpl.loadData("userId", new Date(1000000), new Date(1000000), 100);
//		assertNotEquals(null, data);
	}
}
