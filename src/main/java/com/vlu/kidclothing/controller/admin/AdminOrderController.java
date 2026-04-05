package com.vlu.kidclothing.controller.admin;

import com.vlu.kidclothing.entity.Order;
import com.vlu.kidclothing.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {

    @Autowired private OrderService orderService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "admin/order-list";
    }

    @PostMapping("/update-status")
    public String update(@RequestParam("orderId") Integer id, @RequestParam("status") String status) {
        orderService.updateOrderStatus(id, status);
        return "redirect:/admin/orders";
    }
}