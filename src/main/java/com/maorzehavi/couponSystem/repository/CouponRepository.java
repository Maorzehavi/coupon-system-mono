package com.maorzehavi.couponSystem.repository;

import com.maorzehavi.couponSystem.model.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    @Transactional
    Optional<List<Coupon>> findAllByCategoryId(Long categoryId);

    @Transactional
    Optional<List<Coupon>> findAllByCompanyId(Long companyId);

    @Modifying
    @Transactional
    @Query("update Coupon c set c.amount = c.amount + :amount where c.id = :id")
    void updateCouponAmount(Long id, Integer amount);

    Optional<List<Coupon>> findAllByTitle(String title);

    @Query("select c from Coupon c where c.price <= :maxPrice")
    Optional<List<Coupon>> findAllByPriceLessThan(Double maxPrice);

    @Query("select c.id from Coupon c where c.title = :title and c.company.id = :companyId")
    Optional<Long> getIdByTitleAndCompanyId(String title, Long companyId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "delete from customers_coupons where coupon_id = :couponId")
    void deleteAllCustomersCouponsByCouponId(Long couponId);

    @Transactional
    Boolean existsByTitleAndCompanyId(String title, Long companyId);

    @Query("select c from Coupon c where c.endDate < ?1")
    Optional<List<Coupon>> findAllByEndDateBefore(LocalDate localDate);

    @Transactional
    @Query(nativeQuery = true,
            value = "select * from coupons c where c.id in (select coupon_id from customers_coupons where customer_id = :customerId)    ")
    Optional<List<Coupon>> findAllByCustomerId(Long customerId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,
            value = "insert into customers_coupons (customer_id, coupon_id) values (:customerId, :couponId)")
    void addCouponToCustomer(Long couponId, Long customerId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,
            value = "delete from customers_coupons where customer_id = :customerId and coupon_id = :couponId limit :amount")
    void deleteCouponFromCustomer(Long couponId, Long customerId, Integer amount);

}