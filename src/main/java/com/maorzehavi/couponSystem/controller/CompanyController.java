package com.maorzehavi.couponSystem.controller;

import com.maorzehavi.couponSystem.model.dto.request.ClientRequest;
import com.maorzehavi.couponSystem.model.dto.request.CompanyRequest;
import com.maorzehavi.couponSystem.service.CompanyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
//    @PutMapping()
//    @PreAuthorize("hasAnyRole('ROLE_COMPANY')")
//    public ResponseEntity<?> updateCompany( @RequestBody CompanyRequest companyRequest, Principal principal) {
//        var email = principal.getName();
//       var id = companyService.getIdByEmail(email).orElseThrow(
//                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found"));
//        return ResponseEntity.ok(companyService.updateCompany(id, companyRequest).orElseThrow(
//                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request")
//        ));
//    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_COMPANY')")
    public ResponseEntity<?> deleteCompany(@PathVariable Long id) {
        var companyResponse = companyService.deleteCompany(id);
        return ResponseEntity.ok(companyResponse.orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company not found")
        ));
    }
}
