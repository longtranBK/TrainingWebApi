package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.AvatarImage;

public interface AvatarImageRepository extends JpaRepository<AvatarImage, String> {

}
