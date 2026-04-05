package com.vlu.kidclothing.config;

import com.vlu.kidclothing.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    // Mã hóa mật khẩu
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Cung cấp thông tin User cho Spring Security
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // Cấu hình phân quyền URL
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Tắt CSRF để tránh lỗi khi submit form (nếu bạn chưa cấu hình token)
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        // Các trang ai cũng vào được
                        .requestMatchers("/", "/search", "/category/**", "/product/**", "/cart/**", "/login", "/register", "/css/**", "/js/**", "/images/**").permitAll()
                        // Chỉ tài khoản có role ADMIN mới được vào /admin
                        .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                        // Các request còn lại (như /profile, /checkout) yêu cầu đăng nhập
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login") // Custom trang đăng nhập

                        // FIX TẠI ĐÂY: Đưa về /profile để AuthController tự điều hướng
                        // Admin -> Dashboard | User -> Profile hồng
                        .defaultSuccessUrl("/profile", true)

                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/") // Đăng xuất xong về trang chủ
                        .permitAll()
                );

        return http.build();
    }
}