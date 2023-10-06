package com.example.demo.service;

import java.io.ByteArrayInputStream;
import java.sql.Date;

public interface ReportService {

	ByteArrayInputStream loadData(String userId, Date startDate, Date endDate, int numbersPost);

}
