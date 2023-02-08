package com.maorzehavi.couponSystem.service;

import com.maorzehavi.couponSystem.model.dto.request.ClientRequest;
import com.maorzehavi.couponSystem.model.dto.request.CompanyRequest;
import com.maorzehavi.couponSystem.model.dto.response.CompanyResponse;
import com.maorzehavi.couponSystem.model.entity.Company;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

public interface CompanyService {

    Optional<Long> getIdByName(String name);

    Optional<Long> getIdByEmail(String email);

    Optional<CompanyResponse> getCompany(Long id);

    Optional<Company> getCompanyEntity(Long id);

    Optional<Company> getCompanyEntityByEmail(String email);

    @Modifying
    @Transactional
    Optional<CompanyResponse> createCompany(ClientRequest<CompanyRequest> companyRequest);


    @Modifying
    @Transactional
    Optional<CompanyResponse> updateCompany(ClientRequest<CompanyRequest> companyRequest, Principal principal);

    @Modifying
    @Transactional
    Optional<CompanyResponse> deleteCompany(Long id);

    List<CompanyResponse> getAllCompanies();

    @Modifying
    @Transactional
    void activateCompany(Long id);

    @Modifying
    @Transactional
    void deactivateCompany(Long id);

    // mapping methods
    CompanyResponse mapToCompanyResponse(Company company);

    Company mapToCompany(CompanyRequest companyRequest);

}