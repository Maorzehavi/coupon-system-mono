package com.maorzehavi.couponSystem.service.impl;

import com.maorzehavi.couponSystem.exception.SystemException;
import com.maorzehavi.couponSystem.model.ClientType;
import com.maorzehavi.couponSystem.model.dto.request.ClientRequest;
import com.maorzehavi.couponSystem.model.dto.request.CompanyRequest;
import com.maorzehavi.couponSystem.model.dto.response.CompanyResponse;
import com.maorzehavi.couponSystem.model.entity.Company;
import com.maorzehavi.couponSystem.repository.CompanyRepository;
import com.maorzehavi.couponSystem.service.CompanyService;
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

    private final UserService userService; // this will be a webflux service


    public CompanyServiceImpl(CompanyRepository companyRepository,
                              @Lazy UserService userService) {
        this.companyRepository = companyRepository;

        this.userService = userService;
    }

    @Override
    public Optional<Long> getIdByName(String name) {
        return companyRepository.getIdByCompanyName(name);
    }

    @Override
    public Optional<Long> getIdByEmail(String email) {
        return companyRepository.getIdByCompanyEmail(email);
    }

    @Override
    public Optional<Long> getIdByUserId(Long userId) {
        return companyRepository.getIdByUserId(userId);
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
        return companyRepository.getCompanyByUserEmail(email);
    }

    @Override
    public Optional<CompanyResponse> createCompany(@Valid ClientRequest<CompanyRequest> companyRequest) {
        var company = mapToCompany(companyRequest.getData());
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
                ()-> new SystemException("Company not found")
        );
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
    public Boolean isCompanyActive(Long id) {
        return companyRepository.isActive(id).orElseThrow();
    }

    @Override
    public void activateCompany(Long id) {
        try {
            var company = getCompanyEntity(id).orElseThrow();
            company.setIsActive(true);
            companyRepository.save(company);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deactivateCompany(Long id) {
        try {
            var company = getCompanyEntity(id).orElseThrow();
            company.setIsActive(false);
            companyRepository.save(company);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    @Override
    public Company mapToCompany(CompanyResponse companyResponse)     {
        return Company.builder()
                .id(companyResponse.getId())
                .name(companyResponse.getName())
                .isActive(companyResponse.getIsActive())
                .phoneNumber(companyResponse.getPhoneNumber())
                .build();
    }
}
