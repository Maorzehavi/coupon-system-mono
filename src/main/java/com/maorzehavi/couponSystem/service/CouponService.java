package com.maorzehavi.couponSystem.service;

import com.maorzehavi.couponSystem.model.dto.request.CouponRequest;
import com.maorzehavi.couponSystem.model.dto.response.CouponResponse;
import com.maorzehavi.couponSystem.model.entity.Coupon;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CouponService {
    Optional<Long> getIdByTitleAndCompanyId(String title, Long companyId);

    Optional<CouponResponse> getCoupon(Long id);

    Optional<Coupon> getCouponEntity(Long id);

    Optional<CouponResponse> getCoupon(String title, Long companyId);

    @Transactional
    @Modifying
    Optional<CouponResponse> createCoupon(CouponRequest couponRequest, String companyEmail);

    @Transactional
    @Modifying
    Optional<CouponResponse> updateCoupon(Long id, CouponRequest couponRequest, String companyEmail);

    @Transactional
    @Modifying
    Optional<CouponResponse> deleteCoupon(Long id);

    @Transactional
    @Modifying
    void updateCouponAmount(Long couponId, Integer amount);

    @Transactional
    @Modifying
    void deleteExpiredCoupons();

    @Transactional
    @Modifying
    void deleteAllByCompanyId(Long id);

    @Transactional
    @Modifying
    void deleteAllByCategory(Long categoryId);

    @Transactional
    @Modifying
    void deleteAllByCustomerId(Long id);

    @Transactional
    @Modifying
    void deleteAllFromCustomers(Long couponId);

    void addCouponToCustomer(Long couponId, Long CustomerId);

    void deleteCouponFromCustomer(Long couponId, Long customerId, Integer amount);

    Boolean existsForCompany(String title, Long companyId);

    List<CouponResponse> getAllCoupons();

    List<CouponResponse> getAllCouponsByTitle(String title);

    List<CouponResponse> getAllCouponsByCategory(String categoryName);

    List<CouponResponse> getAllCouponsByCompanyId(Long companyId);


    Coupon mapToCoupon(CouponRequest couponRequest);

    Coupon mapToCoupon(CouponResponse couponResponse);

    CouponResponse mapToCouponResponse(Coupon coupon);


}
