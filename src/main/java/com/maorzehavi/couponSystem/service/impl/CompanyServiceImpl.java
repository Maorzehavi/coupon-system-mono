package com.maorzehavi.couponSystem.service.impl;

import com.maorzehavi.couponSystem.exception.SystemException;
import com.maorzehavi.couponSystem.model.ClientType;
import com.maorzehavi.couponSystem.model.dto.request.ClientRequest;
import com.maorzehavi.couponSystem.model.dto.request.CompanyRequest;
import com.maorzehavi.couponSystem.model.dto.response.CompanyResponse;
import com.maorzehavi.couponSystem.model.entity.Company;
import com.maorzehavi.couponSystem.repository.CompanyRepository;
import com.maorzehavi.couponSystem.service.CompanyService;
import com.maorzehavi.couponSystem.service.CouponService;
import com.maorzehavi.couponSystem.service.UserService;
import jakarta.validation.Valid;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    private final CouponService couponService;  // this will be webflux

    private final UserService userService; // this will be a webflux service


    public CompanyServiceImpl(CompanyRepository companyRepository,
                              @Lazy CouponService couponService,
                              @Lazy UserService userService) {
        this.companyRepository = companyRepository;
        this.couponService = couponService;

        this.userService = userService;
    }

    @Override
    public Optional<Long> getIdByName(String name) {
        return companyRepository.getIdByCompanyName(name);
    }

    @Override
    public Optional<Long> getIdByEmail(String email) {
        return companyRepository.getIdByEmail(email);
    }


    @Override
    public Optional<CompanyResponse> getCompany(Long id) {
        return companyRepository.findById(id).map(this::mapToCompanyResponse);
    }

    @Override
    public Optional<Company> getCompanyEntity(Long id) {
        return companyRepository.findById(id);
    }

    @Override
    public Optional<Company> getCompanyEntityByEmail(String email) {
        return companyRepository.getCompanyByEmail(email);
    }

    @Override
    public Optional<CompanyResponse> createCompany(@Valid ClientRequest<CompanyRequest> companyRequest) {
        var company = mapToCompany(companyRequest.getData());
        if (companyRepository.existsByName(company.getName())) {
            throw new SystemException("Company name already exists");
        }
        var user = userService.createUser(companyRequest.getUser(), ClientType.COMPANY).map(userService::mapToUser)
                .orElseThrow(() -> new SystemException("User creation failed")); // in the future, this will be with webflux
        company.setUser(user);
        company.setIsActive(false);
        return Optional.of(mapToCompanyResponse(companyRepository.save(company)));

    }

    @Override
    public Optional<CompanyResponse> updateCompany(@Valid ClientRequest<CompanyRequest> companyRequest,
                                                   Principal principal) {
        var user = userService.updateUser(companyRequest.getUser(), principal).map(userService::mapToUser)
                .orElseThrow(() -> new SystemException("User update failed"));
        var company = getCompanyEntityByEmail(principal.getName()).orElseThrow();
        company.setName(companyRequest.getData().getName());
        company.setPhoneNumber(companyRequest.getData().getPhoneNumber());
        company.setUser(user);
        return Optional.of(mapToCompanyResponse(companyRepository.save(company)));
    }

    @Override
    public Optional<CompanyResponse> deleteCompany(Long id) {
        var company = getCompanyEntity(id).orElseThrow(
                () -> new SystemException("Company not found")
        );
        userService.deleteUser(company.getUser().getId());
        couponService.deleteAllByCompanyId(id);
        companyRepository.delete(company);
        return Optional.of(mapToCompanyResponse(company));
    }

    @Override
    public List<CompanyResponse> getAllCompanies() {
        return companyRepository.findAll().stream()
                .map(this::mapToCompanyResponse)
                .toList();
    }

    @Override
    public void activateCompany(Long id) {
        companyRepository.updateCompanyActiveStatus(id, true);
    }

    @Override
    public void deactivateCompany(Long id) {
        companyRepository.updateCompanyActiveStatus(id, false);
    }

    @Override
    public CompanyResponse mapToCompanyResponse(Company company) {
        return CompanyResponse.builder()
                .id(company.getId())
                .name(company.getName())
                .isActive(company.getIsActive())
                .phoneNumber(company.getPhoneNumber())
                .email(company.getUser().getEmail())
                .build();
    }

    @Override
    public Company mapToCompany(CompanyRequest companyRequest) {
        return Company.builder()
                .name(companyRequest.getName())
                .phoneNumber(companyRequest.getPhoneNumber())
                .build();
    }

}
