package com.bookshop.demo.service;

import java.util.List;

import com.bookshop.demo.response.OrderDTO;

public interface OrderService {
	List<OrderDTO> getPendingOrders();
	List<OrderDTO> getConfirmedOrders();
	List<OrderDTO> getCompletedOrders();
	
	List<OrderDTO> getPendingOrdersFromUser(Integer userId);
	List<OrderDTO> getConfirmedOrdersFromUser(Integer userId);
	List<OrderDTO> getCompletedOrdersFromUser(Integer userId);
	
	void confirmOrder(Integer orderId);	
	void completedOrder(Integer orderId);	
}
