package com.vlu.kidclothing;

import com.vlu.kidclothing.entity.Category;
import com.vlu.kidclothing.entity.Product;
import com.vlu.kidclothing.entity.User;
import com.vlu.kidclothing.repository.CategoryRepository;
import com.vlu.kidclothing.repository.ProductRepository;
import com.vlu.kidclothing.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class KidclothingApplication {

	public static void main(String[] args) {
		SpringApplication.run(KidclothingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(UserRepository userRepository,
									  CategoryRepository categoryRepository,
									  ProductRepository productRepository,
									  PasswordEncoder passwordEncoder) {
		return args -> {
			// Khởi tạo Admin
			if (userRepository.findByUsername("admin").isEmpty()) {
				User admin = new User();
				admin.setUsername("admin");
				admin.setPassword(passwordEncoder.encode("123456"));
				admin.setFullName("Quản trị viên Hệ thống");
				admin.setEmail("admin@kidclothing.com");
				admin.setPhone("0988888888");
				admin.setRole("ADMIN");
				admin.setMembershipTier("DIAMOND");
				userRepository.save(admin);
			}

			// Ép hệ thống tạo dữ liệu nếu số lượng sản phẩm đang có ít hơn 20 cái
			if (productRepository.count() < 20) {
				System.out.println("====== ĐANG TỰ ĐỘNG BƠM 60 SẢN PHẨM MỚI VÀO DATABASE... ======");

				List<String> categoryNames = Arrays.asList("Quần Áo Bé Trai", "Quần Áo Bé Gái", "Đồ Sơ Sinh", "Phụ Kiện Bé Yêu");

				int imgCounter = 100; // Đổi ID ảnh để lấy ảnh mới
				for (String catName : categoryNames) {
					Category cat = new Category();
					cat.setName(catName);
					Category savedCat = categoryRepository.save(cat);

					// Tạo 15 sản phẩm cho danh mục này
					for (int i = 1; i <= 15; i++) {
						Product p = new Product();
						p.setName("Sản phẩm " + catName + " - Mẫu cao cấp số " + i);

						// Random giá từ 99k đến 399k làm tròn
						double randomPrice = Math.round((99000 + (Math.random() * 300000)) / 1000) * 1000;
						p.setPrice(randomPrice);

						p.setDescription("Chất liệu an toàn tuyệt đối cho làn da nhạy cảm của bé yêu. Thiết kế năng động, thấm hút mồ hôi siêu tốt giúp bé thoải mái vận động cả ngày dài. Hàng chính hãng KidClothing.");

						// Link ảnh ngẫu nhiên chất lượng cao
						p.setImage("https://picsum.photos/seed/" + (imgCounter++) + "/500/500");

						p.setCategory(savedCat);

						// Đánh dấu 4 sản phẩm đầu của mỗi danh mục là Best Seller để đưa ra trang chủ
						p.setIsBestSeller(i <= 4);
						p.setCreatedAt(LocalDateTime.now().minusDays(i));

						productRepository.save(p);
					}
				}
				System.out.println("====== XONG! ĐÃ THÊM THÀNH CÔNG 60 SẢN PHẨM! ======");
			}
		};
	}
}