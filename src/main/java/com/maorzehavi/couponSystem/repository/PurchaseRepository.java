package com.maorzehavi.couponSystem.repository;

import com.maorzehavi.couponSystem.model.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
}