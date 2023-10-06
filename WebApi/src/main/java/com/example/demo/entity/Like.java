package com.example.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.example.demo.entity.primarykey.LikePK;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "likes")
@IdClass(LikePK.class)
public class Like {

	@Id
	@Column(name = "user_id", nullable = false)
	private String userId;

	@Id
	@Column(name = "post_id", nullable = false)
	private String postId;
}
