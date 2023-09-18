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
@Table(name = "users")
public class User {
	
	@Id
	@Column(name = "id", nullable = false)
	private String id;
	
	@Column(name = "full_name", nullable = false)
	private String fullName;
	
	@Column(name = "avatar_url", nullable = false)
	private String avatarUrl;

	@Column(name = "username", nullable = false)
	private String username;
	
	@Column(name = "password", nullable = false)
	private String password;
	
	@Column(name = "role", nullable = false)
	private String role;
	
}	
