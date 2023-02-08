package com.maorzehavi.couponSystem.repository;

import com.maorzehavi.couponSystem.model.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    @Query("select c.id from Company c where c.name = ?1")
    Optional<Long> getIdByCompanyName(String companyName);

    @Query("select c.id from Company c where c.user.email = ?1")
    Optional<Long> getIdByEmail(String companyEmail);

    @Transactional
    @Query("select c from Company c where c.user.email = ?1")
    Optional<Company> getCompanyByEmail(String email);

    Boolean existsByName(String name);

    @Transactional
    @Modifying
    @Query("update Company c set c.isActive = ?2 where c.id = ?1")
    void updateCompanyActiveStatus(Long id, Boolean active);

}
