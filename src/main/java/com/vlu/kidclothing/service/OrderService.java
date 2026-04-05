package com.vlu.kidclothing.service;

import com.vlu.kidclothing.dto.CartItem;
import com.vlu.kidclothing.entity.Order;
import com.vlu.kidclothing.entity.OrderDetail;
import com.vlu.kidclothing.entity.Product;
import com.vlu.kidclothing.entity.User;
import com.vlu.kidclothing.repository.OrderDetailRepository;
import com.vlu.kidclothing.repository.OrderRepository;
import com.vlu.kidclothing.repository.ProductRepository;
import com.vlu.kidclothing.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class OrderService {

    @Autowired private OrderRepository orderRepository;
    @Autowired private OrderDetailRepository orderDetailRepository;
    @Autowired private CartService cartService;
    @Autowired private UserRepository userRepository;

    // ĐÃ THÊM: Gọi kho sản phẩm để lát nữa trừ tồn kho
    @Autowired private ProductRepository productRepository;

    @Transactional
    public Order placeOrder() {
        Order order = new Order();
        order.setTotalPrice(cartService.getFinalAmount());
        order.setStatus("PENDING");
        order.setCreatedAt(LocalDateTime.now());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            User currentUser = userRepository.findByUsername(auth.getName()).orElse(null);
            order.setUser(currentUser);
        }

        Order savedOrder = orderRepository.save(order);

        for (CartItem item : cartService.getCartItems()) {
            // 1. Lưu chi tiết đơn hàng
            OrderDetail detail = new OrderDetail();
            detail.setOrder(savedOrder);

            Product dbProduct = productRepository.findById(item.getProductId()).orElse(null);
            if (dbProduct != null) {
                detail.setProduct(dbProduct);
                detail.setQuantity(item.getQuantity());
                detail.setPrice(item.getPrice());
                orderDetailRepository.save(detail);

                // 2. ĐÃ THÊM LOGIC TRỪ TỒN KHO
                int newStock = dbProduct.getStockQuantity() - item.getQuantity();
                dbProduct.setStockQuantity(newStock < 0 ? 0 : newStock); // Không để âm
                productRepository.save(dbProduct);
            }
        }

        cartService.clear();
        return savedOrder;
    }

    public java.util.List<Order> getAllOrders() {
        return orderRepository.findAll(org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "createdAt"));
    }

    public void updateOrderStatus(Integer orderId, String status) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            order.setStatus(status);
            orderRepository.save(order);
        }
    }
}