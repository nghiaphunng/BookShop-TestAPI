package com.bookshop.demo.service.impl;

import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookshop.demo.entity.User;
import com.bookshop.demo.repository.UserRepository;
import com.bookshop.demo.request.UserLogin;
import com.bookshop.demo.request.UserRegister;
import com.bookshop.demo.service.UserService;
import com.bookshop.demo.utils.ProcessPassword;

@Service
public class UserServiceImpl implements UserService{
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public boolean registerUser(UserRegister userRegister) {
		if(userRepository.existsByUserName(userRegister.getUserName()) || userRepository.existsByUserEmail(userRegister.getUserEmail())) return false;
		else {
			User user = modelMapper.map(userRegister, User.class);
			user.setUserPassword(ProcessPassword.createUserPassword(userRegister.getUserPassword()));
			user.setCreatedAt(LocalDateTime.now());
			userRepository.save(user);
			return true;
		}
	}

	@Override
	public boolean loginUser(UserLogin userLogin) {
		User user = userRepository.findByUserName(userLogin.getUserName());
		if(user == null) return false;
		else {
			return ProcessPassword.encodeUserPassword(userLogin.getUserPassword(), user.getUserPassword());
		}
	}

}
