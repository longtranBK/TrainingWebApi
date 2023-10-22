package com.example.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "comments")
@Table(name = "comments")
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

	@Column(name = "delFlg")
	private boolean delFlg;
}
