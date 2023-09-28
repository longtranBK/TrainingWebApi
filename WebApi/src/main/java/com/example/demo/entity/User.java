package com.example.demo.entity;

import java.util.Set;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
	
	@Column(name = "avatar_url")
	private String avatarUrl;

	@Column(name = "username", nullable = false)
	private String username;
	
	@Column(name = "password", nullable = false)
	private String password;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "reset_password_token")
	private String resetPasswordToken;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "user_roles", 
		joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
		inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
	Set<Role> roles = new HashSet<Role>();
	
    public void addRole(Role role) {
        this.roles.add(role);
    }
    
	@Column(name = "update_ts")
    private Timestamp updateTs;
    
}	
