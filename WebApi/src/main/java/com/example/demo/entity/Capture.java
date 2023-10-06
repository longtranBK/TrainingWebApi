package com.example.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.example.demo.entity.primarykey.CapturePK;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "captures")
@IdClass(CapturePK.class)
public class Capture {

	@Id
	@Column(name = "post_id", nullable = false)
	private String postId;

	@Id
	@Column(name = "capture_url", nullable = false)
	private String captureUrl;
}
