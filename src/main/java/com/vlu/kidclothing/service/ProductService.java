package com.vlu.kidclothing.service;

import com.vlu.kidclothing.entity.Product;
import com.vlu.kidclothing.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // Lấy tất cả sản phẩm
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Lấy chi tiết 1 sản phẩm theo ID
    public Product getProductById(Integer id) {
        return productRepository.findById(id).orElse(null);
    }

    // Lưu sản phẩm (Thêm mới / Cập nhật)
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    // Xóa sản phẩm
    public void deleteProduct(Integer id) {
        productRepository.deleteById(id);
    }

    // Lấy danh sách sản phẩm thuộc 1 danh mục cụ thể
    public List<Product> getProductsByCategory(Integer categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    // Xử lý logic tìm kiếm sản phẩm cho ô Search
    public List<Product> searchProducts(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }
}