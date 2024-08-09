package com.bookshop.demo.service;

import java.util.List;

import com.bookshop.demo.response.BookDTO;

public interface BookService {
	List<BookDTO> getListBook();
	boolean addToCart(Integer userId, Integer bookId, Integer quantity);
	boolean deleteToCart(Integer userId, Integer bookId, Integer quantity);
}
