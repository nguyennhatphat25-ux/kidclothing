package com.vlu.kidclothing.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "coupons")
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String code; // Mã nhập vào, VD: FREESHIP

    @Column(name = "discount_amount", nullable = false)
    private Double discountAmount; // Số tiền được giảm

    @Column(name = "expiry_date")
    private LocalDate expiryDate; // Hạn sử dụng
}