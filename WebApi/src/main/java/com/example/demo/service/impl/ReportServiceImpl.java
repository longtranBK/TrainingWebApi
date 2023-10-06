package com.example.demo.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.entity.Comment;
import com.example.demo.entity.Post;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserFriendRepository;
import com.example.demo.service.ReportService;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ReportServiceImpl implements ReportService{
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private UserFriendRepository userFriendRepository;
	
	@Autowired
	private PostRepository postRepository;

	@Override
	public ByteArrayInputStream loadData(String userId, Date startDate, Date endDate, int numbersPost) {
		String sheetName = "ConsumptionInfor";
		String[] HEADERs = { "Post numbers", "New friend numbers", "Comment numbers" };
		
		List<Post> postList = postRepository.getPostsCustom(userId, startDate, endDate, numbersPost);
		List<String> userIdFriendList = userFriendRepository.getUserIdFriendList(userId, startDate, endDate);
		List<Comment> commentList = commentRepository.getCommentList(startDate, endDate);
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet(sheetName);
		
		
		return null;
	}

}
