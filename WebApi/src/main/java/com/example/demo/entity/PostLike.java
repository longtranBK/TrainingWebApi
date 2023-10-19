package com.example.demo.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.example.demo.entity.primarykey.PostLikePK;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "post_likes")
@IdClass(PostLikePK.class)
public class PostLike {

	@Id
	@Column(name = "user_id", nullable = false)
	private String userId;

	@Id
	@Column(name = "post_id", nullable = false)
	private String postId;

	@Column(name = "create_ts", nullable = false)
	private Timestamp createTs;
}
