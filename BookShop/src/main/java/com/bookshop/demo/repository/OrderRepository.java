package com.bookshop.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bookshop.demo.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Integer>{
	@Query(value = "SELECT confirm_order(:orderId)", nativeQuery = true)
    void confirmOrder(@Param("orderId") Integer orderId);
	
	@Query(value = "SELECT mark_order_as_completed(:orderId)", nativeQuery = true)
    void completedOrder(@Param("orderId") Integer orderId);
	
	@Query(value = "SELECT * FROM orders WHERE status = 'Pending'", nativeQuery = true)
    List<Order> findPendingOrders();
	
	@Query(value = "SELECT * FROM orders WHERE status = 'Confirmed'", nativeQuery = true)
    List<Order> findConfirmedOrders();
	
	@Query(value = "SELECT * FROM orders WHERE status = 'Completed'", nativeQuery = true)
    List<Order> findCompletedOrders();
	
	@Query(value = "SELECT * FROM orders WHERE status = 'Pending' AND user_id = :userId", nativeQuery = true)
    List<Order> findPendingOrdersFromUser(Integer userId);
	
	@Query(value = "SELECT * FROM orders WHERE status = 'Confirmed' AND user_id = :userId", nativeQuery = true)
    List<Order> findConfirmedOrdersFromUser(Integer userId);
	
	@Query(value = "SELECT * FROM orders WHERE status = 'Completed' AND user_id = :userId", nativeQuery = true)
    List<Order> findCompletedOrdersFromUser(Integer userId);
}
