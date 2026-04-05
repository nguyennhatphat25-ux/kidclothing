package com.vlu.kidclothing.controller.user;

import com.vlu.kidclothing.dto.CartItem;
import com.vlu.kidclothing.entity.Coupon;
import com.vlu.kidclothing.entity.Order;
import com.vlu.kidclothing.entity.Product;
import com.vlu.kidclothing.service.CartService;
import com.vlu.kidclothing.service.CouponService;
import com.vlu.kidclothing.service.OrderService;
import com.vlu.kidclothing.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired private CartService cartService;
    @Autowired private ProductService productService;
    @Autowired private OrderService orderService;
    @Autowired private CouponService couponService;

    @GetMapping
    public String viewCart(Model model) {
        model.addAttribute("cartItems", cartService.getCartItems());
        model.addAttribute("totalAmount", cartService.getAmount());
        model.addAttribute("discountAmount", cartService.getDiscountAmount());
        model.addAttribute("finalAmount", cartService.getFinalAmount());
        return "user/cart";
    }

    @GetMapping("/add/{id}")
    public String addToCart(@PathVariable("id") Integer id) {
        Product product = productService.getProductById(id);
        if (product != null) {
            CartItem item = new CartItem();
            item.setProductId(product.getId());
            item.setName(product.getName());
            item.setPrice(product.getPrice());
            item.setImage(product.getImage());
            item.setQuantity(1);
            cartService.add(item);
        }
        return "redirect:/cart";
    }

    @PostMapping("/update")
    public String updateCart(@RequestParam("productId") Integer productId, @RequestParam("quantity") int quantity) {
        cartService.update(productId, quantity);
        return "redirect:/cart";
    }

    @GetMapping("/remove/{id}")
    public String removeCartItem(@PathVariable("id") Integer id) {
        cartService.remove(id);
        return "redirect:/cart";
    }

    @PostMapping("/apply-coupon")
    public String applyCoupon(@RequestParam("code") String code, Model model) {
        Coupon coupon = couponService.getByCode(code);
        if (coupon != null && (coupon.getExpiryDate() == null || !coupon.getExpiryDate().isBefore(java.time.LocalDate.now()))) {
            cartService.applyDiscount(coupon.getDiscountAmount());
        }
        return "redirect:/cart";
    }

    @GetMapping("/checkout")
    public String checkout(Model model) {
        if (cartService.getCartItems().isEmpty()) { return "redirect:/cart"; }
        Order newOrder = orderService.placeOrder();

        // TRUYỀN CẢ ĐỐI TƯỢNG ORDER RA ĐỂ LẤY NGÀY GIỜ VÀ THÔNG TIN KHÁCH
        model.addAttribute("order", newOrder);

        return "user/checkout-success";
    }
}