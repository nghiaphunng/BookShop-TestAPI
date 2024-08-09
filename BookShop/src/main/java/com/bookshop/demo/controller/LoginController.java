package com.bookshop.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.bookshop.demo.request.UserLogin;
import com.bookshop.demo.service.UserService;

@Controller
public class LoginController {
	@Autowired
	private UserService userService;
	
	@PostMapping(value = {"/login"})
	public ResponseEntity<Map<String, String>> loginUser(@RequestBody UserLogin userLogin){
		boolean check = userService.loginUser(userLogin);
		Map<String, String> result = new HashMap<>();
		result.put("login result", String.valueOf(check));
		return ResponseEntity.ok(result);
	}
}
