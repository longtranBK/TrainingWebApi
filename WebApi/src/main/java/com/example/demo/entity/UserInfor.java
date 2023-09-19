package com.example.demo.entity;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfor {
	
	@Id
	@Column(name = "id", nullable = false)
	private String id;
	
	@Column(name = "is_active", nullable = false)
	private int isActive;
	
	@Column(name = "study_at")
	private String studyAt;
	
	@Column(name = "working_at")
	private String workingAt;
	
	@Column(name = "favorites")
	private String favorites;
	
	@Column(name = "other_info")
	private String otherInfo;
	
	@Column(name = "date_of_birth", nullable = false)
	private Date dateOfBirth;
	
	@Column(name = "create_ts", nullable = false)
	private Timestamp createTs;
	
	@Column(name = "create_ts")
	private Timestamp updateTs;
	
}
