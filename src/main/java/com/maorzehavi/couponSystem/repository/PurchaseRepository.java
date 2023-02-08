package com.maorzehavi.couponSystem.repository;

import com.maorzehavi.couponSystem.model.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    @Transactional
    Optional<List<Purchase>> findAllByCustomerId(Long id);

    @Transactional
    @Query(nativeQuery = true,value =
    "SELECT * FROM purchases WHERE id in (SELECT p.purchase_id FROM purchases_coupons p WHERE coupon_id = ?1)")
    Optional<List<Purchase>> findAllByCouponId(Long id);

    @Transactional
    @Query(nativeQuery = true,value =
    "SELECT * FROM purchases WHERE id in " +
            "(SELECT p.purchase_id FROM purchases_coupons p WHERE coupon_id in " +
            "(SELECT id FROM coupons WHERE company_id = ?1))")
    Optional<List<Purchase>> findAllByCompanyId(Long id);
}