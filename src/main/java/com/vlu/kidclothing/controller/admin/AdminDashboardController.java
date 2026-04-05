package com.vlu.kidclothing.controller.admin;

import com.vlu.kidclothing.repository.OrderRepository;
import com.vlu.kidclothing.repository.ProductRepository;
import com.vlu.kidclothing.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminDashboardController {

    @Autowired private OrderRepository orderRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private UserRepository userRepository;

    @GetMapping("/admin/dashboard")
    public String dashboard(Model model) {
        // Thống kê số lượng
        model.addAttribute("totalOrders", orderRepository.count());
        model.addAttribute("totalProducts", productRepository.count());
        model.addAttribute("totalUsers", userRepository.count());

        // Tính tổng doanh thu (Chỉ tính các đơn hàng đã hoàn thành - DELIVERED)
        double revenue = orderRepository.findAll().stream()
                .filter(o -> "DELIVERED".equals(o.getStatus()))
                .mapToDouble(o -> o.getTotalPrice() != null ? o.getTotalPrice() : 0)
                .sum();
        model.addAttribute("totalRevenue", revenue);

        return "admin/dashboard";
    }
}