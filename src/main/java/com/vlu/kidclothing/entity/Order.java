package com.vlu.kidclothing.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "total_price")
    private Double totalPrice;

    private String status = "PENDING"; // Trạng thái: Chờ xử lý

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    // Tạm thời cho phép khách vãng lai đặt hàng (user_id có thể null)
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;
}