package com.bookshop.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.bookshop.demo.request.UserRegister;
import com.bookshop.demo.service.UserService;

@Controller
public class RegisterController {
	@Autowired
	private UserService userService;
	
	@PostMapping(value = {"/register"})
	public ResponseEntity<Map<String, String>> registerUser(@RequestBody UserRegister userRegister){
		boolean check = userService.registerUser(userRegister);
		Map<String, String> result = new HashMap<>();
		result.put("register result", String.valueOf(check));
		return ResponseEntity.ok(result);
	}
}
