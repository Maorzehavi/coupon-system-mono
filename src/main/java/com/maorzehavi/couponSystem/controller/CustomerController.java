package com.maorzehavi.couponSystem.controller;

import com.maorzehavi.couponSystem.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE-CUSTOMER')")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id){
        try{
            customerService.deleteCustomer(id);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
