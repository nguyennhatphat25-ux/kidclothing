package com.vlu.kidclothing.repository;

import com.vlu.kidclothing.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}