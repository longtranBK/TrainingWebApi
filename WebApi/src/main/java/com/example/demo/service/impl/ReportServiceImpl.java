package com.example.demo.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.repository.CommentLikeRepository;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PostLikeRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserFriendRepository;
import com.example.demo.service.ReportService;

@Service
public class ReportServiceImpl implements ReportService {

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private UserFriendRepository userFriendRepository;

	@Autowired
	private PostLikeRepository postLikeRepository;

	@Autowired
	private CommentLikeRepository commentLikeRepository;

	@Override
	public ByteArrayInputStream loadData(String userId, Date startDate, Date endDate) {
		try {
			String sheetName = "ConsumptionInfor";
			String[] headers = { "Post numbers", "New friend numbers", "Like numbers", "Comment numbers" };

			int postNumbers = postRepository.countPostWithTime(userId, startDate, endDate);
			int newFriendNumbers = userFriendRepository.countNewFriendWithTime(userId, startDate, endDate);
			int likeNumbers = postLikeRepository.countLikeWithTime(userId, startDate, endDate)
					+ commentLikeRepository.countLikeWithTime(userId, startDate, endDate);
			int commentNumbers = commentRepository.countCommentWithTime(userId, startDate, endDate);

			Workbook workbook = new XSSFWorkbook();
			Sheet sheet = workbook.createSheet(sheetName);

			Row headerRow = sheet.createRow(0);

			for (int col = 0; col < headers.length; col++) {
				Cell cell = headerRow.createCell(col);
				cell.setCellValue(headers[col]);
			}

			Row row = sheet.createRow(1);
			row.createCell(0).setCellValue(postNumbers);
			row.createCell(1).setCellValue(newFriendNumbers);
			row.createCell(2).setCellValue(likeNumbers);
			row.createCell(3).setCellValue(commentNumbers);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			workbook.write(out);
			workbook.close();

			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			throw new RuntimeException("Fail to import data to Excel file: " + e.getMessage());
		}
	}
}
