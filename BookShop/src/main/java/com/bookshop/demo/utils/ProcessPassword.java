package com.bookshop.demo.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class ProcessPassword {
	private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
	
	public static String createUserPassword(String passWord) {
		return passwordEncoder.encode(passWord);
	}
	public static boolean isPasswordMatch(String passWord, String passWordDB) {
		return passwordEncoder.matches(passWord, passWordDB);
	}
}
