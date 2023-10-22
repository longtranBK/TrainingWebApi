package com.example.demo.service;

import java.io.ByteArrayInputStream;
import java.sql.Date;

public interface ReportService {

	/**
	 * Get data then load to ByteArrayInputStream
	 * 
	 * @param userId
	 * @param startDate
	 * @param endDate
	 * @return ByteArrayInputStream
	 */
	ByteArrayInputStream loadData(String userId, Date startDate, Date endDate);

}
