package com.maorzehavi.couponSystem.repository;

import com.maorzehavi.couponSystem.model.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Optional<Coupon> findByTitle(String title);

    Optional<Coupon> findCouponByTitleAndCompanyId(String title, Long id);

    @Query("select c from Coupon c join fetch c.category where c.id = :id")
    Optional<Coupon> findByIdWithCategory(Long id);

    @Query("select c from Coupon c join fetch c.company where c.id = :id")
    Optional<Coupon> findByIdWithCompany(Long id);

    @Query("select c from Coupon c join fetch c.company join fetch c.category where c.id = :id")
    Optional<Coupon> findByIdWithCompanyAndCategory(Long id);

    @Query("select c from Coupon c left join c.category where c.category in (select c from Category c where c.id = :categoryId)")
    Optional<List<Coupon>> findAllByCategoryId(Long categoryId);

    //    @Query("select c from Coupon c join fetch c.company  where c.company in (select c from Company c where c.id = :companyId)")
//@Query("select c from Coupon c where c.company.id = ?1")
    Optional<List<Coupon>> findAllByCompanyId(Long companyId);

    @Transactional
    @Query("delete from Coupon c where c.category in (select c from Category c where c.id = :categoryId)")
    @Modifying
    void deleteAllByCategoryId(Long categoryId);

    @Transactional
    @Query("delete from Coupon c where c.company in (select c from Company c where c.id = :id)")
    @Modifying
    void deleteAllByCompanyId(Long id);

    @Modifying
    @Transactional
    @Query("update Coupon c set c.amount = c.amount + :amount where c.id = :id")
    void updateCouponAmount(Long id, Integer amount);

    @Query("select c from Coupon c where c.price <= :maxPrice")
    Optional<List<Coupon>> findAllByPriceLessThan(Double maxPrice);

    @Query("select c.id from Coupon c where c.title = :title and c.company.id = :companyId")
    Optional<Long> getIdByTitleAndCompanyId(String title, Long companyId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "delete from customers_coupons where coupon_id = :couponId")
    void deleteAllCustomersCouponsByCouponId(Long couponId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "delete from customers_coupons where customer_id = :customerId")
    void deleteAllCustomersCouponsByCustomerId(Long customerId);

    Boolean existsByTitleAndCompanyId(String title, Long companyId);
}