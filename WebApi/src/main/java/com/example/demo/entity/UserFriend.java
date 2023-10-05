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
@Table(name = "user_friend")
public class UserFriend {
	
	@Id
	@Column(name = "id", nullable = false)
	private String id;
	
	@Column(name = "user1", nullable = false)
	private String user1;
	
	@Column(name = "user2", nullable = false)
	private String user2;
	

}
