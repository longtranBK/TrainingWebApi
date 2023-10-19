package com.example.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "avatar_image")
public class AvatarImage {
	
	@Id
	@Column(name = "user_id", nullable = false)
	private String userId;

	@Column(name = "image_url")
	private String imageUrl;
}
