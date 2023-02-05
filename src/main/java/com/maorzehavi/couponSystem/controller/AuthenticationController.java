package com.maorzehavi.couponSystem.controller;

import com.maorzehavi.couponSystem.model.ClientType;
import com.maorzehavi.couponSystem.model.dto.request.CompanyRequest;
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

    private final UserService userService;

    private final CompanyService companyService;

    public AuthenticationController(AuthenticationService authenticationService, UserService userService, CompanyService companyService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.companyService = companyService;
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
    public ResponseEntity<?> createCompany(@RequestBody CompanyRequest companyRequest,
                                           @RequestBody UserRequest request){
        User user = userService.createUser(request, ClientType.COMPANY).map(userService::mapToUser).orElseThrow();
        Optional<CompanyResponse> company = companyService.createCompany(companyRequest, user);
        return ResponseEntity.ok(company);
    }

}
