package com.bookshop.demo.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookshop.demo.entity.Book;
import com.bookshop.demo.entity.User;
import com.bookshop.demo.repository.BookRepository;
import com.bookshop.demo.repository.UserRepository;
import com.bookshop.demo.response.BookDTO;
import com.bookshop.demo.service.BookService;

@Service
public class BookServiceImpl implements BookService{
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ModelMapper modelMapper; 
	
	@Override
	public List<BookDTO> getListBook() {
		List<Book> listBook = bookRepository.findAll();
		return listBook.stream().map(book -> modelMapper.map(book, BookDTO.class)).collect(Collectors.toList());
	}

	@Override
	public boolean addToCart(Integer userId, Integer bookId, Integer quantity) {
		User user = userRepository.findByUserId(userId);
		if(user == null) return false;
		
		Book book = bookRepository.findByBookId(bookId);
		if(book == null) return false;
		
		bookRepository.addToCart(userId, bookId, quantity);
		return true;
	}

	@Override
	public boolean deleteToCart(Integer userId, Integer bookId, Integer quantity) {
		User user = userRepository.findByUserId(userId);
		if(user == null) return false;
		
		Book book = bookRepository.findByBookId(bookId);
		if(book == null) return false;
		
		bookRepository.deleteToCart(userId, bookId, quantity);
		return true;
	}
}
