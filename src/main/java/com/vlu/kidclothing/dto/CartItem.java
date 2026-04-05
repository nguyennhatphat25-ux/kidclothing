package com.vlu.kidclothing.dto;

import lombok.Data;

@Data
public class CartItem {
    private Integer productId;
    private String name;
    private Double price;
    private int quantity = 1; // Mặc định số lượng là 1 khi thêm vào
    private String image;
}