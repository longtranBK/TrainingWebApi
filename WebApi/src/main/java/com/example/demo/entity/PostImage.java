package com.example.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.example.demo.entity.primarykey.PostImagePK;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "post_image")
@IdClass(PostImagePK.class)
public class PostImage {

	@Id
	@Column(name = "post_id", nullable = false)
	private String postId;

	@Id
	@Column(name = "image_url", nullable = false)
	private String imageUrl;
}
