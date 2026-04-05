package com.vlu.kidclothing.service;

import com.vlu.kidclothing.dto.CartItem;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@SessionScope
@Service
public class CartService {
    private Map<Integer, CartItem> map = new HashMap<>();
    private double discountAmount = 0; // Biến lưu số tiền được giảm

    public void add(CartItem item) {
        CartItem existedItem = map.get(item.getProductId());
        if(existedItem != null) {
            existedItem.setQuantity(item.getQuantity() + existedItem.getQuantity());
        } else {
            map.put(item.getProductId(), item);
        }
    }

    public void remove(Integer productId) { map.remove(productId); }

    public void update(Integer productId, int quantity) {
        CartItem item = map.get(productId);
        if(item != null) { item.setQuantity(quantity); }
    }

    public Collection<CartItem> getCartItems() { return map.values(); }

    public double getAmount() {
        return map.values().stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
    }

    // Các hàm xử lý giảm giá
    public void applyDiscount(double amount) { this.discountAmount = amount; }
    public double getDiscountAmount() { return this.discountAmount; }
    public double getFinalAmount() {
        double total = getAmount() - discountAmount;
        return total > 0 ? total : 0; // Đảm bảo không bị âm tiền
    }

    public void clear() {
        map.clear();
        discountAmount = 0; // Reset mã giảm giá sau khi đặt hàng
    }
}