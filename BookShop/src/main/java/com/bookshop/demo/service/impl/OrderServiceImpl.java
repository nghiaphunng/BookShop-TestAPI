package com.bookshop.demo.service.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookshop.demo.entity.Order;
import com.bookshop.demo.repository.OrderRepository;
import com.bookshop.demo.response.BookCartDTO;
import com.bookshop.demo.response.OrderDTO;
import com.bookshop.demo.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService{
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public List<OrderDTO> getPendingOrders() {
		List<Order> orders = orderRepository.findPendingOrders();
		
		return orders.stream()
				.map(order -> {
					OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);

					Set<BookCartDTO> bookCartDTOs = order.getOrderItems().stream()
						.map(orderItem -> modelMapper.map(orderItem, BookCartDTO.class))
						.collect(Collectors.toSet());
					orderDTO.setBooks(bookCartDTOs);
					
					return orderDTO;
				})
			.collect(Collectors.toList());
	}

	@Override
	public void confirmOrder(Integer orderId) {
		orderRepository.confirmOrder(orderId);
	}

	@Override
	public List<OrderDTO> getConfirmedOrders() {
		List<Order> orders = orderRepository.findConfirmedOrders();
		
		return orders.stream()
				.map(order -> {
					OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);

					Set<BookCartDTO> bookCartDTOs = order.getOrderItems().stream()
						.map(orderItem -> modelMapper.map(orderItem, BookCartDTO.class))
						.collect(Collectors.toSet());
					orderDTO.setBooks(bookCartDTOs);
					
					return orderDTO;
				})
			.collect(Collectors.toList());
	}

	@Override
	public List<OrderDTO> getCompletedOrders() {
		List<Order> orders = orderRepository.findCompletedOrders();
		
		return orders.stream()
				.map(order -> {
					OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);

					Set<BookCartDTO> bookCartDTOs = order.getOrderItems().stream()
						.map(orderItem -> modelMapper.map(orderItem, BookCartDTO.class))
						.collect(Collectors.toSet());
					orderDTO.setBooks(bookCartDTOs);
					
					return orderDTO;
				})
			.collect(Collectors.toList());
	}

	@Override
	public void completedOrder(Integer orderId) {
		orderRepository.completedOrder(orderId);		
	}

	@Override
	public List<OrderDTO> getPendingOrdersFromUser(Integer userId) {
		List<Order> orders = orderRepository.findPendingOrdersFromUser(userId);
		
		return orders.stream()
				.map(order -> {
					OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);

					Set<BookCartDTO> bookCartDTOs = order.getOrderItems().stream()
						.map(orderItem -> modelMapper.map(orderItem, BookCartDTO.class))
						.collect(Collectors.toSet());
					orderDTO.setBooks(bookCartDTOs);
					
					return orderDTO;
				})
			.collect(Collectors.toList());
	}

	@Override
	public List<OrderDTO> getConfirmedOrdersFromUser(Integer userId) {
		List<Order> orders = orderRepository.findConfirmedOrdersFromUser(userId);
		
		return orders.stream()
				.map(order -> {
					OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);

					Set<BookCartDTO> bookCartDTOs = order.getOrderItems().stream()
						.map(orderItem -> modelMapper.map(orderItem, BookCartDTO.class))
						.collect(Collectors.toSet());
					orderDTO.setBooks(bookCartDTOs);
					
					return orderDTO;
				})
			.collect(Collectors.toList());
	}

	@Override
	public List<OrderDTO> getCompletedOrdersFromUser(Integer userId) {
		List<Order> orders = orderRepository.findCompletedOrdersFromUser(userId);
		
		return orders.stream()
				.map(order -> {
					OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);

					Set<BookCartDTO> bookCartDTOs = order.getOrderItems().stream()
						.map(orderItem -> modelMapper.map(orderItem, BookCartDTO.class))
						.collect(Collectors.toSet());
					orderDTO.setBooks(bookCartDTOs);
					
					return orderDTO;
				})
			.collect(Collectors.toList());
	}

}
