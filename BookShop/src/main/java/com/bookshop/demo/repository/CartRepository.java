package com.bookshop.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bookshop.demo.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Integer>{
	@Query(value = "SELECT confirm_cart(:userId)", nativeQuery = true)
    void confirmCart(@Param("userId") Integer userId);
}
