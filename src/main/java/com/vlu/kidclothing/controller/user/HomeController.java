package com.vlu.kidclothing.controller.user;

import com.vlu.kidclothing.entity.Category;
import com.vlu.kidclothing.entity.Product;
import com.vlu.kidclothing.service.CategoryService;
import com.vlu.kidclothing.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    // Hàm này chạy ngầm: Tự động tải danh sách Danh Mục vào mọi trang HTML
    // để hiển thị thanh Menu ngang (Trang chủ, Bé Trai, Bé Gái...)
    @ModelAttribute("menuCategories")
    public List<Category> getMenuCategories() {
        return categoryService.getAllCategories();
    }

    // 1. TRANG CHỦ (index.html)
    @GetMapping("/")
    public String home(Model model) {
        // Lấy tất cả sản phẩm hiển thị ra trang chủ
        List<Product> newProducts = productService.getAllProducts();
        model.addAttribute("newProducts", newProducts);
        return "user/index";
    }

    // 2. TRANG DANH MỤC SẢN PHẨM (shop.html) - Đã fix lỗi gạch chân menu
    @GetMapping("/category/{id}")
    public String categoryProducts(@PathVariable("id") Integer id, Model model) {
        Category category = categoryService.getById(id);

        if (category != null) {
            model.addAttribute("pageTitle", category.getName());

            // --- DÒNG QUAN TRỌNG NHẤT ---
            // Truyền đối tượng category hiện tại ra giao diện HTML
            // HTML sẽ lấy ID này so sánh với ID của Menu để vẽ gạch chân màu hồng
            model.addAttribute("category", category);
        }

        // Truyền danh sách sản phẩm thuộc danh mục này
        model.addAttribute("products", productService.getProductsByCategory(id));

        return "user/shop";
    }

    // 3. TRANG TÌM KIẾM
    @GetMapping("/search")
    public String searchProducts(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<Product> searchResults;

        if (keyword != null && !keyword.trim().isEmpty()) {
            searchResults = productService.searchProducts(keyword);
            model.addAttribute("pageTitle", "Kết quả tìm kiếm cho: '" + keyword + "'");
        } else {
            searchResults = productService.getAllProducts();
            model.addAttribute("pageTitle", "Tất cả sản phẩm");
        }

        model.addAttribute("products", searchResults);

        // Dùng chung giao diện shop.html để hiển thị kết quả tìm kiếm cho đồng bộ
        return "user/shop";
    }
}