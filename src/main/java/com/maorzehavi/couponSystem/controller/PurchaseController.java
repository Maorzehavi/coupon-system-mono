package com.maorzehavi.couponSystem.controller;

import com.maorzehavi.couponSystem.model.dto.request.PurchaseRequest;
import com.maorzehavi.couponSystem.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/purchase")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    @PostMapping
    public ResponseEntity<?> createPurchase(@RequestBody PurchaseRequest purchaseRequest, Principal principal){
        try{
            return ResponseEntity.ok(purchaseService.createPurchase(purchaseRequest,principal.getName()));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getPurchase(@PathVariable Long id){
        try{
            return ResponseEntity.ok(purchaseService.getPurchase(id));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllPurchases(){
        try{
            return ResponseEntity.ok(purchaseService.getAllPurchases());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("customer/{id}")
    public ResponseEntity<?> getAllPurchasesByCustomerId(@PathVariable Long id){
        var purchases = purchaseService.getAllPurchasesByCustomerId(id);
        if (purchases.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(purchases);
    }

    @GetMapping("coupon/{id}")
    public ResponseEntity<?> getAllPurchasesByCouponId(@PathVariable Long id){
      var purchases = purchaseService.getAllPurchasesByCouponId(id);
        if (purchases.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(purchases);
    }

    @GetMapping("company/{id}")
    public ResponseEntity<?> getAllPurchasesByCompanyId(@PathVariable Long id){
        var purchases = purchaseService.getAllPurchasesByCompanyId(id);
        if (purchases.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(purchases);
    }

}
