package com.maorzehavi.couponSystem.controller;

import com.maorzehavi.couponSystem.model.dto.request.CouponRequest;
import com.maorzehavi.couponSystem.service.CouponService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/vi/coupon")
public class CouponController {

    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_COMPANY')")
    public ResponseEntity<?> createCoupon(@RequestBody CouponRequest couponRequest, Principal principal){
        try {
            return ResponseEntity.ok(couponService.createCoupon(couponRequest,principal.getName()));
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllCoupons(){
        try{
            return ResponseEntity.ok(couponService.getAllCoupons());
        } catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{title}")
    public ResponseEntity<?> getCouponByTitle(@PathVariable String title){
        try{
            return ResponseEntity.ok(couponService.getAllCouponsByTitle(title));
        } catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/company")
    public ResponseEntity<?> getCouponsByCompany(Principal principal){
        try{
            return ResponseEntity.ok(couponService.getAllCouponsByCompany(principal.getName()));
        } catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }


}
