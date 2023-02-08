package com.maorzehavi.couponSystem.repository;

import com.maorzehavi.couponSystem.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.DoubleStream;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("select c.id from Category c where c.name = ?1")
    Optional<Long> getIdByName(String name);

    Boolean existsByName(String name);

    @Transactional
    @Query("select c from Category c where c.id in (select c.category.id from Coupon c where c.id = :id)")
    Optional<Category> findCategoryByCouponId(Long id);

    Optional<Category> findByName(String name);
}