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
@Table(name = "captures")
public class Capture {
	
	@Id
	@Column(name = "id", nullable = false)
	private String captureId;
	
	@Column(name = "post_id", nullable = false)
	private String postId;
	
	@Column(name = "capture_url", nullable = false)
	private String captureUrl;
}
