package com.example.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "likes")
public class Like {

	@Id
	@Column(name = "id", nullable = false)
	private String likeId;

	@Column(name = "user_id", nullable = false)
	private String user_id;

	@Column(name = "post_id", nullable = false)
	private String postId;
}
