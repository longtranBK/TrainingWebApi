package com.example.demo.entity;

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
@Table(name = "posts")
public class Post {
	
	@Id
	@Column(name = "id", nullable = false)
	private String postId;
	
	@Column(name = "user_id", nullable = false)
	private String userId;
	
	@Column(name = "content")
	private String content;

	@Column(name = "status", nullable = false)
	private String status;
	
	@Column(name = "password", nullable = false)
	private String password;
	
	@Column(name = "update_ts")
    private Timestamp updateTs;
}
