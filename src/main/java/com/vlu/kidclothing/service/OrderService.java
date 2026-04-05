package com.vlu.kidclothing.service;

import com.vlu.kidclothing.dto.CartItem;
import com.vlu.kidclothing.entity.Order;
import com.vlu.kidclothing.entity.OrderDetail;
import com.vlu.kidclothing.entity.Product;
import com.vlu.kidclothing.repository.OrderDetailRepository;
import com.vlu.kidclothing.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private CartService cartService;

    @Transactional
    public Order placeOrder() {
        // 1. Lưu thông tin Order (bảng cha)
        Order order = new Order();
        order.setTotalPrice(cartService.getAmount());
        order.setStatus("PENDING");
        // Ở đồ án này, ta tạm cho khách vãng lai mua hàng không cần login để demo cho nhanh
        Order savedOrder = orderRepository.save(order);

        // 2. Lưu từng sản phẩm trong giỏ vào OrderDetail (bảng con)
        for (CartItem item : cartService.getCartItems()) {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(savedOrder);

            Product product = new Product();
            product.setId(item.getProductId());
            detail.setProduct(product);

            detail.setQuantity(item.getQuantity());
            detail.setPrice(item.getPrice());

            orderDetailRepository.save(detail);
        }

        // 3. Xóa sạch giỏ hàng sau khi đặt thành công
        cartService.clear();

        return savedOrder;
    }
    // Lấy toàn bộ danh sách đơn hàng (Sắp xếp mới nhất lên đầu)
    public java.util.List<Order> getAllOrders() {
        return orderRepository.findAll(org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "createdAt"));
    }

    // Cập nhật trạng thái giao hàng
    public void updateOrderStatus(Integer orderId, String status) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            order.setStatus(status);
            orderRepository.save(order);
        }
    }
}