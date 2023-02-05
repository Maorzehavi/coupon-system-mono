package com.maorzehavi.couponSystem.service;

import com.maorzehavi.couponSystem.model.dto.request.CompanyRequest;
import com.maorzehavi.couponSystem.model.dto.response.CompanyResponse;
import com.maorzehavi.couponSystem.model.entity.Company;
import com.maorzehavi.couponSystem.model.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CompanyService {

    Optional<Long> getIdByName(String name);

    Optional<Long> getIdByEmail(String email);

    Optional<Long> getIdByUserId(Long userId);

    Optional<CompanyResponse> getCompany(Long id);

    Optional<Company> getCompanyEntity(Long id);

    @Modifying
    @Transactional
    Optional<CompanyResponse> createCompany(CompanyRequest companyRequest, User user);


    @Modifying
    @Transactional
    Optional<CompanyResponse> updateCompany(Long id, CompanyRequest companyRequest);

    @Modifying
    @Transactional
    Optional<CompanyResponse> deleteCompany(Long id);

    List<CompanyResponse> getAllCompanies();

    Boolean isCompanyActive(Long id);

    @Modifying
    @Transactional
    void activateCompany(Long id);

    @Modifying
    @Transactional
    void deactivateCompany(Long id);

    // mapping methods
    CompanyResponse mapToCompanyResponse(Company company);

    Company mapToCompany(CompanyRequest companyRequest);

    Company mapToCompany(CompanyResponse companyResponse);

}