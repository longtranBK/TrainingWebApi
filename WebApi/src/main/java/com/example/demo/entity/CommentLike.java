package com.example.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.example.demo.entity.primarykey.CommentLikePK;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "comment_likes")
@IdClass(CommentLikePK.class)
public class CommentLike {

	@Id
	@Column(name = "user_id", nullable = false)
	private String userId;

	@Id
	@Column(name = "comment_id", nullable = false)
	private String commentId;

}