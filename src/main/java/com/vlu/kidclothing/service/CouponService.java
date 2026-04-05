package com.vlu.kidclothing.service;

import com.vlu.kidclothing.entity.Coupon;
import com.vlu.kidclothing.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CouponService {
    @Autowired
    private CouponRepository couponRepository;

    public List<Coupon> getAll() { return couponRepository.findAll(); }
    public Coupon save(Coupon coupon) { return couponRepository.save(coupon); }
    public Coupon getById(Integer id) { return couponRepository.findById(id).orElse(null); }
    public void delete(Integer id) { couponRepository.deleteById(id); }
    public Coupon getByCode(String code) { return couponRepository.findByCode(code).orElse(null); }
}