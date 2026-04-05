package com.vlu.kidclothing.controller.admin;

import com.vlu.kidclothing.entity.Product;
import com.vlu.kidclothing.service.CategoryService;
import com.vlu.kidclothing.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

    @Autowired
    private ProductService productService;

    // Gọi thêm CategoryService để lấy danh sách danh mục
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "admin/product-list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        // Truyền danh sách categories sang form HTML
        model.addAttribute("categories", categoryService.getAll());
        return "admin/product-form";
    }

    @PostMapping("/save")
    public String saveProduct(@ModelAttribute("product") Product product) {
        productService.saveProduct(product);
        return "redirect:/admin/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("product", productService.getProductById(id));
        // Truyền danh sách categories sang form HTML để sửa
        model.addAttribute("categories", categoryService.getAll());
        return "admin/product-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Integer id) {
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }
}