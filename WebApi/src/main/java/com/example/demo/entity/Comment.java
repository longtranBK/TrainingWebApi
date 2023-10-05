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
@Table(name = "comment")
public class Comment {
	
	@Id
	@Column(name = "id", nullable = false)
	private String commentId;
	
	@Column(name = "user_id", nullable = false)
	private String userId;
	
	@Column(name = "post_id", nullable = false)
	private String postId;
	
	@Column(name = "content", nullable = false)
	private String content;
	
	@Column(name = "create_ts", nullable = false)
	private Timestamp createTs;
	
	@Column(name = "update_ts", nullable = false)
	private Timestamp updateTs;
}
