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
	private String userId;
	
	@Column(name = "full_name")
	private String fullName;
	
	@Column(name = "avatar_url")
	private String avatarUrl;
	
	@Column(name = "is_active")
	private int isActive;
	
	@Column(name = "sex")
	private String sex;
	
	@Column(name = "study_at")
	private String studyAt;
	
	@Column(name = "working_at")
	private String workingAt;
	
	@Column(name = "favorites")
	private String favorites;
	
	@Column(name = "other_infor")
	private String otherInfor;
	
	@Column(name = "date_of_birth")
	private Date dateOfBirth;
	
}
