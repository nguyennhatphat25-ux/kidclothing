package com.vlu.kidclothing.service;

import com.vlu.kidclothing.entity.Category;
import com.vlu.kidclothing.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // Hàm này dành cho HomeController hiển thị Menu
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // ĐÃ THÊM LẠI HÀM NÀY: Dành cho các trang Admin gọi lấy danh sách
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    // Lấy chi tiết 1 danh mục theo ID
    public Category getById(Integer id) {
        return categoryRepository.findById(id).orElse(null);
    }

    // Lưu danh mục (Thêm mới / Cập nhật)
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    // Xóa danh mục
    public void delete(Integer id) {
        categoryRepository.deleteById(id);
    }
}