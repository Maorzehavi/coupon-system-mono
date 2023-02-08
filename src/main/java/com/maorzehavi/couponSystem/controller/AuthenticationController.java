package com.maorzehavi.couponSystem.controller;

import com.maorzehavi.couponSystem.model.ClientType;
import com.maorzehavi.couponSystem.model.dto.request.ClientRequest;
import com.maorzehavi.couponSystem.model.dto.request.CompanyRequest;
import com.maorzehavi.couponSystem.model.dto.request.CustomerRequest;
import com.maorzehavi.couponSystem.model.dto.request.UserRequest;
import com.maorzehavi.couponSystem.model.dto.response.CompanyResponse;
import com.maorzehavi.couponSystem.model.entity.User;
import com.maorzehavi.couponSystem.security.service.AuthenticationService;
import com.maorzehavi.couponSystem.service.CompanyService;
import com.maorzehavi.couponSystem.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequest request){
        try{
            return ResponseEntity.ok((authenticationService.authenticate(request)));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(){
        try{
            authenticationService.logout();
            return ResponseEntity.ok("Logout successful");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/register-company")
    public ResponseEntity<?> registerCompany(@RequestBody ClientRequest<CompanyRequest> request){
        try{
            return ResponseEntity.ok(authenticationService.registerCompany(request));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/register-customer")
    public ResponseEntity<?> registerCustomer(@RequestBody ClientRequest<CustomerRequest> request){
        try{
            return ResponseEntity.ok(authenticationService.registerCustomer(request));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
