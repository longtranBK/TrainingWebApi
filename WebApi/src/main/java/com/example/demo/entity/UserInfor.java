package com.example.demo.entity;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_infor")
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
	
	@Column(name = "other_infor")
	private String otherInfor;
	
	@Column(name = "date_of_birth", nullable = false)
	private Date dateOfBirth;
	
}
