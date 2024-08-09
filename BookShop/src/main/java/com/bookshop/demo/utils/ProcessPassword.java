package com.bookshop.demo.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class ProcessPassword {
	public static String createUserPassword(String passWord) {
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
		return passwordEncoder.encode(passWord);
	}
	public static boolean encodeUserPassword(String passWord, String passWordDB) {
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
		return passwordEncoder.matches(passWord, passWordDB);
	}
}
