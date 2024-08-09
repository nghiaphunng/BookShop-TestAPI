package com.bookshop.demo.service;

import java.util.List;

import com.bookshop.demo.response.BookCartDTO;

public interface CartService {
	List<BookCartDTO> getBooksInCart(Integer userId);
	boolean confirmCart(Integer userId);
}
