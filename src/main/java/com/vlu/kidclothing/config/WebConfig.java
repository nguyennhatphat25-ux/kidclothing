package com.vlu.kidclothing.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Biến thư mục "uploads" trong máy tính thành đường dẫn ảo /images/uploads/ trên web
        // Mẹo: Dùng tiền tố /images/ để tự động lọt qua chốt chặn của SecurityConfig
        registry.addResourceHandler("/images/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}