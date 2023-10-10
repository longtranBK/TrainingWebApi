package com.example.demo.service.impl;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.example.demo.dto.request.InsertCommentReqDto;
import com.example.demo.entity.Comment;
import com.example.demo.repository.CommentRepository;

@SpringBootTest
public class CommentServiceImplTest {
	@MockBean
	private CommentRepository commentRepository;

	@Autowired
	private CommentServiceImpl commentServiceImpl;

	@Test
	void insertComment_withInput_excuteSuccess() {
		InsertCommentReqDto input = new InsertCommentReqDto();
		Comment comment = new Comment();

		when(commentRepository.save(any())).thenReturn(comment);
		commentServiceImpl.insertComment(input);
	}

	@Test
	void findByCommentId_withInput_returnComment() {
		Comment comment = new Comment();
		when(commentRepository.findByCommentId(any())).thenReturn(comment);

		Comment result = commentServiceImpl.findByCommentId("id");
		assertNotEquals(null, result);
	}

	@Test
	void updateComment_withInput_excuteSuccess() {
		Comment comment = new Comment();

		when(commentRepository.save(any())).thenReturn(comment);
		commentServiceImpl.updateComment(comment, "content");
	}

	@Test
	void deleteComment_withInput_excuteSuccess() {
		Comment comment = new Comment();

		when(commentRepository.save(any())).thenReturn(comment);
		commentServiceImpl.deleteComment(comment);
	}

}
