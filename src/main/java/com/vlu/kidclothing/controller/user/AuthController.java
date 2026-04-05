package com.vlu.kidclothing.controller.user;

import com.vlu.kidclothing.entity.User;
import com.vlu.kidclothing.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.security.Principal;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String showLoginForm() {
        return "user/login";
    }

    @GetMapping("/register")
    public String showRegisterForm() {
        return "user/register";
    }

    @PostMapping("/register")
    public String processRegister(@RequestParam("username") String username,
                                  @RequestParam("password") String password,
                                  @RequestParam("email") String email,
                                  @RequestParam("phone") String phone,
                                  @RequestParam("fullName") String fullName,
                                  Model model) {
        if (password.length() < 6) {
            model.addAttribute("error", "Mật khẩu phải dài trên 6 ký tự!");
            return "user/register";
        }
        if (userRepository.findByUsername(username).isPresent()) {
            model.addAttribute("error", "Tên đăng nhập đã tồn tại!");
            return "user/register";
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setEmail(email);
        newUser.setPhone(phone);
        newUser.setFullName(fullName);
        newUser.setRole("USER");
        newUser.setMembershipTier("MEMBER");
        userRepository.save(newUser);
        return "redirect:/login?registered=true";
    }

    @GetMapping("/profile")
    public String viewProfile(Principal principal, Model model) {
        if (principal == null) return "redirect:/login";

        User user = userRepository.findByUsername(principal.getName()).orElse(null);

        // PHÂN LUỒNG: Nếu là Admin thì vào thẳng Dashboard
        if (user != null && "ADMIN".equals(user.getRole())) {
            return "redirect:/admin/dashboard";
        }

        model.addAttribute("user", user);
        int discount = 0;
        if(user != null) {
            switch(user.getMembershipTier()) {
                case "SILVER": discount = 5; break;
                case "GOLD": discount = 10; break;
                case "DIAMOND": discount = 15; break;
                default: discount = 0;
            }
        }
        model.addAttribute("discount", discount);
        return "user/profile";
    }
}