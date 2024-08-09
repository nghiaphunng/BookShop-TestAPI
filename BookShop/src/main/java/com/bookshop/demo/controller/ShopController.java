package com.bookshop.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.bookshop.demo.response.OrderDTO;
import com.bookshop.demo.service.OrderService;


@Controller
@RequestMapping("/shop")
public class ShopController {
	@Autowired
	private OrderService orderService;
	
	//danh sách đơn hàng cần xác nhận
	@GetMapping(value = {"", "/"})
	public ResponseEntity<List<OrderDTO>> getListPendingOrders() {
		List<OrderDTO> res = orderService.getPendingOrders();
		return ResponseEntity.ok(res);
	}
	
	//xác nhận đơn hàng = vận chuyển
	@GetMapping(value = {"/confirm-order/{orderId}"})
	public ResponseEntity<String> getConfirmOderByShop(@PathVariable(name = "orderId") Integer orderId) {
		try{
			orderService.confirmOrder(orderId);
			return ResponseEntity.ok("Order confirmed successfully");
		}
		catch (Exception e) {
			return ResponseEntity.ok("Order confirmed failed");
		}
	}
	
	//danh sách sách đã nhận hàng thành công
	@GetMapping(value = {"/received-books"})
	public ResponseEntity<List<OrderDTO>> getListReceivedOrders() {
		List<OrderDTO> res = orderService.getCompletedOrders();
		return ResponseEntity.ok(res);
	}
	
}
