package com.bookshop.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bookshop.demo.entity.Book;

public interface BookRepository extends JpaRepository<Book, Integer>, CustomizedBookRepository{
	Book findByBookId(Integer bookId);
	
    @Query(value = "SELECT add_to_cart(:userId, :bookId, :quantity)", nativeQuery = true)
    void addToCart(@Param("userId") Integer userId, @Param("bookId") Integer bookId, @Param("quantity") Integer quantity);
    
    @Query(value = "SELECT remove_from_cart(:userId, :bookId, :quantity)", nativeQuery = true)
    void deleteToCart(@Param("userId") Integer userId, @Param("bookId") Integer bookId, @Param("quantity") Integer quantity);
}
