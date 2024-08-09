package com.bookshop.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bookshop.demo.response.BookDTO;
import com.bookshop.demo.service.BookService;

@Controller
@RequestMapping(value = {"/home"})
public class HomeController {
	@Autowired
	private BookService bookService;
	
	@GetMapping(value = {"/", "", "/list-book"})
	public ResponseEntity<List<BookDTO>> listBook(){
		try {
	        List<BookDTO> books = bookService.getListBook();
	        return ResponseEntity.ok(books);
	    } catch (Exception e) {
	    	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}
	
	
}
