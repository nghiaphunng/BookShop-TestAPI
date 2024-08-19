0.Đăng ký tài khoản 
	http://localhost:8084/register
	{
	    "userName": "user4",
	    "userPassword": "123456",
	    "userEmail": "user4@gmail.com"
	}

1.Danh sách thông tin sản phẩm trên trang chủ (GET)
	http://localhost:8084/home
	http://localhost:8084/home/
	http://localhost:8084/home/list-book

2.Lấy token xác thực (GET)
	http://localhost:8084/home/get-token
	{
	    "userName": "user4",
	    "userPassword": "123456"
	}

3.Xác thực token (POST)
	http://localhost:8084/home/introspect
	{
	    "token": ""
	}

4.1.Danh sách sản phẩm trong giỏ (GET)
	http://localhost:8084/cart/{userId}

4.2.Thêm sản phẩm vào giỏ hàng (GET)
	http://localhost:8084/cart/add?userId=3&bookId=2&quantity=4

4.3.Xóa sản phẩm khỏi giỏ hàng (GET)
	http://localhost:8084/cart/delete?userId=3&bookId=3&quantity=4

4.4.Xác nhận đơn hàng (GET)
	http://localhost:8084/cart/confirm-cart/3

4.5.Danh sách đơn hàng đang đợi xác nhận từ shop (GET)
	http://localhost:8084/pending-books?userId=3

4.6.Dánh sách đơn hàng đang được giao (GET)
	http://localhost:8084/shipping-books?userId=3

4.7.Xác nhận đã nhận hàng thành công (GET)
	http://localhost:8084/complete/{orderId}

4.8.Danh sách các đơn hàng đã được giao thành công 
	http://localhost:8084/received-books?userId=?

5.1.Danh sách đơn hàng cần xác nhận (GET)
	http://localhost:8084/shop
	http://localhost:8084/shop/

5.2.Xác nhận đơn hàng để gửi đi (GET) 
	http://localhost:8084/shop/confirm-order/3

5.3.Danh sách các đơn hàng đã được giao thành công 
	http://localhost:8084/received-books


