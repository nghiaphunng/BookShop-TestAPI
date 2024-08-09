package com.bookshop.demo.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bookshop.demo.entity.Order;
import com.bookshop.demo.entity.OrderItem;
import com.bookshop.demo.response.BookCartDTO;
import com.bookshop.demo.response.OrderDTO;

@Configuration
public class ModelMapperConfig {
	@Bean
	ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		
		//chuyển Order -> OrderDTO 
		modelMapper.typeMap(Order.class, OrderDTO.class).addMappings(mapper -> {
            mapper.map(src -> src.getUser().getUserName(), OrderDTO::setUserName);
            mapper.map(Order::getOrderId, OrderDTO::setOrderId);
        });
		
		//chuyển OrderItem -> BookCartDTO
		modelMapper.typeMap(OrderItem.class, BookCartDTO.class).addMappings(mapper -> {
            mapper.map(src -> src.getBook().getBookAuthor(), BookCartDTO::setBookAuthor);
            mapper.map(src -> src.getBook().getBookTitle(), BookCartDTO::setBookTitle);
            mapper.map(src -> src.getBook().getBookPrice(), BookCartDTO::setBookPrice);
            mapper.map(src ->src.getQuantity(), BookCartDTO::setQuantity);
            mapper.map(src -> src.getBook().getBookId(), BookCartDTO::setBookId);
        });
		
		return modelMapper;
	}
}
