package com.vlu.kidclothing.repository;

import com.vlu.kidclothing.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Integer> {
    Optional<Coupon> findByCode(String code); // Tìm mã giảm giá theo chuỗi code khách nhập
}