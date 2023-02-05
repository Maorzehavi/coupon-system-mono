package com.maorzehavi.couponSystem.repository;

import com.maorzehavi.couponSystem.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("select c.id from Customer c where c.user.email = ?1")
    @Transactional
    Optional<Long> findCustomerIdByEmail(String email);


}