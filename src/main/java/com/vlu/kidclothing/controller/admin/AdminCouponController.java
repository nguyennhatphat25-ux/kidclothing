package com.vlu.kidclothing.controller.admin;

import com.vlu.kidclothing.entity.Coupon;
import com.vlu.kidclothing.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/coupons")
public class AdminCouponController {

    @Autowired
    private CouponService couponService;

    @GetMapping
    public String listCoupons(Model model) {
        model.addAttribute("coupons", couponService.getAll());
        return "admin/coupon-list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("coupon", new Coupon());
        return "admin/coupon-form";
    }

    @PostMapping("/save")
    public String saveCoupon(@ModelAttribute("coupon") Coupon coupon) {
        couponService.save(coupon);
        return "redirect:/admin/coupons";
    }

    @GetMapping("/delete/{id}")
    public String deleteCoupon(@PathVariable("id") Integer id) {
        couponService.delete(id);
        return "redirect:/admin/coupons";
    }
}