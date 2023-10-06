package com.example.demo.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.example.demo.entity.primarykey.CommentPK;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "comment")
@IdClass(CommentPK.class)
public class Comment {

	@Id
	@Column(name = "user_id", nullable = false)
	private String userId;

	@Id
	@Column(name = "post_id", nullable = false)
	private String postId;

	@Column(name = "content", nullable = false)
	private String content;

	@Column(name = "create_ts", nullable = false)
	private Timestamp createTs;

	@Column(name = "update_ts", nullable = false)
	private Timestamp updateTs;
}
