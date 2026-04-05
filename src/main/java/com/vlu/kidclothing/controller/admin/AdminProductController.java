package com.vlu.kidclothing.controller.admin;

import com.vlu.kidclothing.entity.Product;
import com.vlu.kidclothing.service.CategoryService;
import com.vlu.kidclothing.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

    @Autowired private ProductService productService;
    @Autowired private CategoryService categoryService;

    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "admin/product-list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAll());
        return "admin/product-form";
    }

    @PostMapping("/save")
    public String saveProduct(@ModelAttribute("product") Product formProduct,
                              @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        try {
            // 1. Nếu không nhập ảnh mới thì phục hồi ảnh cũ
            if (formProduct.getId() != null) {
                Product existingProduct = productService.getProductById(formProduct.getId());
                if (existingProduct != null) {
                    if ((formProduct.getImage() == null || formProduct.getImage().isEmpty())
                            && (imageFile == null || imageFile.isEmpty())) {
                        formProduct.setImage(existingProduct.getImage());
                    }
                }
            }

            // 2. LOGIC LƯU ẢNH ĐÃ ĐƯỢC FIX LỖI
            if (imageFile != null && !imageFile.isEmpty()) {
                // Tạo tên file độc nhất để không bị trùng
                String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename().replaceAll("\\s+", "_");

                // Dùng Paths lấy vị trí thư mục gốc của project đang chạy
                Path uploadPath = Paths.get("uploads");

                // Tạo thư mục nếu chưa có
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Copy file từ form vào thư mục uploads an toàn
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // Cập nhật đường dẫn ảo cho ảnh
                formProduct.setImage("/images/uploads/" + fileName);
            }

            // Lưu sản phẩm vào DB
            productService.saveProduct(formProduct);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/admin/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("product", productService.getProductById(id));
        model.addAttribute("categories", categoryService.getAll());
        return "admin/product-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Integer id) {
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }
}