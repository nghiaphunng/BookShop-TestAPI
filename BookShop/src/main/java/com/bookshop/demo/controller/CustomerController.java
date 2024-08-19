package com.bookshop.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bookshop.demo.response.BookCartDTO;
import com.bookshop.demo.response.OrderDTO;
import com.bookshop.demo.service.BookService;
import com.bookshop.demo.service.CartService;
import com.bookshop.demo.service.OrderService;

@Controller
public class CustomerController {
	@Autowired
	private BookService bookService;
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private OrderService orderService;
	
	//danh sách sách trong giỏ hàng
	@GetMapping(value = {"/cart/{userId}"})
	public ResponseEntity<List<BookCartDTO>> addBook(@PathVariable(name = "userId") Integer userId){
		List<BookCartDTO> books = cartService.getBooksInCart(userId);
		return ResponseEntity.ok(books);
	}
	
	//thêm sách vào giỏ hàng
	@GetMapping(value = {"/cart/add"})
	public ResponseEntity<Map<String, String>> addBook(
			@RequestParam(name = "userId") Integer userId,
			@RequestParam(name ="bookId") Integer bookId,
			@RequestParam(name = "quantity") Integer quantity){
		boolean check = bookService.addToCart(userId, bookId, quantity);
		Map<String, String> result = new HashMap<>();
		result.put("Add cart result", String.valueOf(check));
		return ResponseEntity.ok(result);
	}
	
	//xóa sách ở giỏ hàng
	@GetMapping(value = {"/cart/delete"})
	public ResponseEntity<Map<String, String>> deleteBook(
			@RequestParam(name = "userId") Integer userId,
			@RequestParam(name ="bookId") Integer bookId,
			@RequestParam(name = "quantity") Integer quantity){
		boolean check = bookService.deleteToCart(userId, bookId, quantity);
		Map<String, String> result = new HashMap<>();
		result.put("Add cart result", String.valueOf(check));
		return ResponseEntity.ok(result);
	}
	
	//xác nhận đơn hàng
	@GetMapping(value = {"/cart/confirm-cart/{userId}"})
	public ResponseEntity<String> confirmCart(@PathVariable(name = "userId") Integer userId) {
        boolean check = cartService.confirmCart(userId);
        
        if (check) {
            return ResponseEntity.ok("Cart confirmed successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to confirm cart");
        }
    }
	
	//danh sách sách đang đợi xác nhận từ shop
	@GetMapping(value = {"/pending-books"})
	public ResponseEntity<List<OrderDTO>> getListPendingOrdersFromUser(@RequestParam(name = "userId") Integer userId) {
		List<OrderDTO> res = orderService.getPendingOrdersFromUser(userId);
		return ResponseEntity.ok(res);
	}
	
	//danh sách sách đang giao hàng = shop đã xác nhận
	@GetMapping(value = {"/shipping-books"})
	public ResponseEntity<List<OrderDTO>> getListShippingOrdersFromUser(@RequestParam(name = "userId") Integer userId) {
		List<OrderDTO> res = orderService.getConfirmedOrdersFromUser(userId);
		return ResponseEntity.ok(res);
	}
	
	//danh sách sách đã nhận hàng thành công
	@GetMapping(value = {"/received-books"})
	public ResponseEntity<List<OrderDTO>> getListReceivedOrdersFromUser(@RequestParam(name = "userId") Integer userId) {
		List<OrderDTO> res = orderService.getCompletedOrdersFromUser(userId);
		return ResponseEntity.ok(res);
	}
	
	//xác nhận đã nhận hàng thành công
	@GetMapping(value = {"/complete/{orderId}"})
	public ResponseEntity<String> getListConfirmCompleteOrders(@PathVariable(name = "orderId") Integer orderId) {
		try{
			orderService.completedOrder(orderId);
			return ResponseEntity.ok("Order received successfully");
		}
		catch (Exception e) {
			return ResponseEntity.ok("Order received failed");
		}
	}
}
