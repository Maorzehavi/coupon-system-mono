package com.maorzehavi.couponSystem.controller;

import com.maorzehavi.couponSystem.model.dto.request.PurchaseRequest;
import com.maorzehavi.couponSystem.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
