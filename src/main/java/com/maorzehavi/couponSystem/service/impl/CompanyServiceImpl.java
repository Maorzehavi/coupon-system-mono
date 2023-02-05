package com.maorzehavi.couponSystem.service.impl;

import com.maorzehavi.couponSystem.model.dto.request.CompanyRequest;
import com.maorzehavi.couponSystem.model.dto.response.CompanyResponse;
import com.maorzehavi.couponSystem.model.entity.Company;
import com.maorzehavi.couponSystem.model.entity.User;
import com.maorzehavi.couponSystem.repository.CompanyRepository;
import com.maorzehavi.couponSystem.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
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
    public Optional<CompanyResponse> createCompany(@Valid CompanyRequest companyRequest,
                                                   @NonNull User user) {
        var company = mapToCompany(companyRequest);
        company.setUser(user);
        company.setIsActive(false);
        return Optional.of(mapToCompanyResponse(companyRepository.save(company)));
    }

    @Override
    public Optional<CompanyResponse> updateCompany(Long id,
                                                   @Valid CompanyRequest companyRequest) {
        var company = getCompanyEntity(id).orElseThrow();
        company.setName(companyRequest.getName());
        company.setPhoneNumber(companyRequest.getPhoneNumber());
        return Optional.of(mapToCompanyResponse(companyRepository.save(company)));
    }

    @Override
    public Optional<CompanyResponse> deleteCompany(Long id) {
        var company = getCompanyEntity(id).orElseThrow();
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
