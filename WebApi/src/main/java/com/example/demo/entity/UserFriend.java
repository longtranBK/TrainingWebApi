package com.example.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.example.demo.entity.primarykey.UserFriendPK;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_friend")
@IdClass(UserFriendPK.class)
public class UserFriend {
	
	@Id
	@Column(name = "user1", nullable = false)
	private String user1;
	
	@Id
	@Column(name = "user2", nullable = false)
	private String user2;
	
	@Column(name = "status")
	private String status;
	
}
