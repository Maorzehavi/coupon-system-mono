package com.maorzehavi.couponSystem.repository;

import com.maorzehavi.couponSystem.model.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    @Transactional
    @Query("select c.id from Company c where c.user.id = ?1")
    Optional<Long> getIdByUserId(Long userId);

    @Query("select c.id from Company c where c.name = ?1")
    Optional<Long> getIdByCompanyName(String companyName);

    @Query("select c.id from Company c where c.user.email = ?1")
    Optional<Long> getIdByCompanyEmail(String companyEmail);

    @Transactional
    @Query("select c from Company c where c.user.email = ?1")
    Optional<Company> getCompanyByUserEmail(String email);

    Boolean existsByName(String name);

    @Query("select c.isActive from Company c where c.id = ?1")
    Optional<Boolean> isActive(Long id);
}
