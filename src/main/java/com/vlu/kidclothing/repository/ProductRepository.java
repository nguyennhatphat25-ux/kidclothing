package com.vlu.kidclothing.repository;

import com.vlu.kidclothing.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    // Tìm danh sách sản phẩm theo ID của danh mục
    List<Product> findByCategoryId(Integer categoryId);

    // Tìm kiếm sản phẩm theo từ khóa (có chứa từ khóa và không phân biệt hoa/thường)
    List<Product> findByNameContainingIgnoreCase(String keyword);
}