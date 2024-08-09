package com.bookshop.demo.service;

import com.bookshop.demo.request.UserLogin;
import com.bookshop.demo.request.UserRegister;

public interface UserService {
	boolean registerUser(UserRegister userRegister);
	boolean	loginUser(UserLogin userLogin);
}
