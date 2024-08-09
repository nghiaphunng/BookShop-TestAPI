package com.bookshop.demo.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookshop.demo.entity.CartItem;
import com.bookshop.demo.entity.User;
import com.bookshop.demo.repository.CartItemRepository;
import com.bookshop.demo.repository.CartRepository;
import com.bookshop.demo.repository.UserRepository;
import com.bookshop.demo.response.BookCartDTO;
import com.bookshop.demo.service.CartService;

@Service
public class CartServiceImpl implements CartService{
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartItemRepository cartItemRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public List<BookCartDTO> getBooksInCart(Integer userId) {
		List<CartItem> cartItems = cartItemRepository.findByCartUserUserId(userId);
		return cartItems.stream()
                .map(cartItem -> {
                    BookCartDTO dto = modelMapper.map(cartItem, BookCartDTO.class);
                    dto.setBookTitle(cartItem.getBook().getBookTitle());
                    dto.setBookAuthor(cartItem.getBook().getBookAuthor());
                    dto.setBookPrice(cartItem.getBook().getBookPrice());
                    dto.setQuantity(cartItem.getQuantity());
                    return dto;
                })
                .collect(Collectors.toList());
	}

	@Override
	public boolean confirmCart(Integer userId) {
		User user = userRepository.findByUserId(userId);
		if(user == null) return false;

		cartRepository.confirmCart(userId);
		return true;
	}
}
