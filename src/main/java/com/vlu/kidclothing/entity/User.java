package com.vlu.kidclothing.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String fullName;

    // Các trường mới thêm
    private String email;
    private String phone;

    // Hạng thành viên: MEMBER, SILVER, GOLD, DIAMOND
    private String membershipTier = "MEMBER";

    @Column(columnDefinition = "ENUM('ADMIN', 'USER') DEFAULT 'USER'")
    private String role;
}