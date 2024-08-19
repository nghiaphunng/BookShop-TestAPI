package com.bookshop.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bookshop.demo.request.AuthenticationRequest;
import com.bookshop.demo.request.IntrospectRequest;
import com.bookshop.demo.response.AuthenticationResponse;
import com.bookshop.demo.response.BookDTO;
import com.bookshop.demo.response.IntrospectResponse;
import com.bookshop.demo.service.AuthenticationService;
import com.bookshop.demo.service.BookService;

@Controller
@RequestMapping(value = {"/home"})
public class HomeController {
	@Autowired
	private BookService bookService;
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@GetMapping(value = {"/", "", "/list-book"})
	public ResponseEntity<List<BookDTO>> listBook(){
		try {
	        List<BookDTO> books = bookService.getListBook();
	        return ResponseEntity.ok(books);
	    } catch (Exception e) {
	    	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}
	
	@GetMapping(value = {"/get-token"})
	@ResponseBody
	public AuthenticationResponse getToken(@RequestBody AuthenticationRequest request) {
		return authenticationService.authenticate(request);
	}
	
	@PostMapping(value = {"/introspect"}) //xác thực token
	@ResponseBody
	public IntrospectResponse introspect(@RequestBody IntrospectRequest request) {
		return authenticationService.introspect(request);
	}
}
