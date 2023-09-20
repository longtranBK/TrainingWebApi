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
@Table(name = "roles")
public class Role {
	
	@Id
	@Column(name = "id", nullable = false)
	private String id;
	
	@Column(name = "role_name", nullable = false)
	private String roleName;
}
