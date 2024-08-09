package com.bookshop.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookshop.demo.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	boolean existsByUserName(String userName);
	boolean existsByUserEmail(String userEmail);
	User findByUserName(String userName);
	User findByUserId(Integer userId);
}
