package com.vlu.kidclothing.repository;

import com.vlu.kidclothing.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}