package com.maorzehavi.couponSystem.controller;

import com.maorzehavi.couponSystem.model.dto.request.ClientRequest;
import com.maorzehavi.couponSystem.model.dto.request.CompanyRequest;
import com.maorzehavi.couponSystem.service.CompanyService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/company")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAllCompanies() {
        return ResponseEntity.ok(companyService.getAllCompanies());
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getCompanyById(@PathVariable Long id) {
        return ResponseEntity.ok(companyService.getCompany(id));
    }

    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('ROLE_COMPANY')")
    public ResponseEntity<?> updateCompany(@RequestBody ClientRequest<CompanyRequest> request, Principal principal) {
        try {
            return ResponseEntity.ok(companyService.updateCompany(request, principal).orElseThrow());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        }
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_COMPANY')")
    public ResponseEntity<?> deleteCompany(@PathVariable Long id) {
        try {
            var companyResponse = companyService.deleteCompany(id);
            return ResponseEntity.ok(companyResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('ROLE_COMPANY')")
    public ResponseEntity<?> deleteCompany(Principal principal) {
        long id = companyService.getIdByEmail(principal.getName()).orElseThrow();
        return deleteCompany(id);
    }

    @PutMapping("/activate/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<?> activateCompany(@PathVariable Long id) {
        try {
            companyService.activateCompany(id);
            return ResponseEntity.ok().body("Company activated");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/deactivate/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<?> deactivateCompany(@PathVariable Long id) {
        try {
            companyService.deactivateCompany(id);
            return ResponseEntity.ok().body("Company deactivated");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/test/{id}")
    public ResponseEntity<?> test(@PathVariable Long id) {
        return ResponseEntity.ok().body("test"+id);
    }
}
