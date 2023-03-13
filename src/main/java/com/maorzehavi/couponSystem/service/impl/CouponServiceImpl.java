package com.maorzehavi.couponSystem.service.impl;

import com.maorzehavi.couponSystem.exception.SystemException;
import com.maorzehavi.couponSystem.model.dto.request.CouponRequest;
import com.maorzehavi.couponSystem.model.dto.response.CouponResponse;
import com.maorzehavi.couponSystem.model.entity.Coupon;
import com.maorzehavi.couponSystem.repository.CouponRepository;
import com.maorzehavi.couponSystem.service.CategoryService;
import com.maorzehavi.couponSystem.service.CompanyService;
import com.maorzehavi.couponSystem.service.CouponService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    @Qualifier("companyServiceImpl")
    private final CompanyService companyService; // this will be a webflux service
    @Qualifier("categoryServiceImpl")
    private final CategoryService categoryService; // this will be a webflux service


    public CouponServiceImpl(CouponRepository couponRepository,
                             @Lazy CompanyService companyService,
                             @Lazy CategoryService categoryService) {
        this.couponRepository = couponRepository;
        this.companyService = companyService;
        this.categoryService = categoryService;
    }

    @Override
    public Optional<Long> getIdByTitleAndCompanyId(String title, Long companyId) {
        return couponRepository.getIdByTitleAndCompanyId(title, companyId);
    }

    @Override
    public Optional<CouponResponse> getCoupon(Long id) {
        return couponRepository.findById(id).map(this::mapToCouponResponse);
    }

    @Override
    public Optional<Coupon> getCouponEntity(Long id) {
        return couponRepository.findById(id);
    }

    @Override
    public Optional<CouponResponse> getCoupon(String title, Long companyId) {
        long id = couponRepository.getIdByTitleAndCompanyId(title, companyId).orElseThrow(
                () -> new SystemException("Not found coupon with title: " + title + " and company id: " + companyId));
        return getCoupon(id);
    }

    @Override
    public Optional<CouponResponse> createCoupon(CouponRequest couponRequest, String companyEmail) {
        var company = companyService.getCompanyEntityByEmail(companyEmail).orElseThrow(
                () -> new SystemException("Company with email '" + companyEmail + "' not found")
        );
        if (!company.getIsActive()) {
            throw new SystemException(("Company '" + company.getName() + "' not active yet"));
        }
        if (existsForCompany(couponRequest.getTitle(), company.getId())) {
            throw new SystemException("Coupon with title: " + couponRequest.getTitle() + " already exists for company: " + company.getName());
        }
        var category = categoryService.getCategoryEntity(couponRequest.getCategoryId()).orElseThrow(
                () -> new SystemException("Not found category with id: " + couponRequest.getCategoryId()));
        var coupon = mapToCoupon(couponRequest);
        coupon.setCategory(category);
        coupon.setCompany(company);
        return Optional.of(mapToCouponResponse(couponRepository.save(coupon)));
    }

    @Override
    public Optional<CouponResponse> updateCoupon(Long id, CouponRequest couponRequest, String companyEmail) {
        var coupon = couponRepository.findById(id).orElseThrow(
                () -> new SystemException("Not found coupon with id: " + id));
        var company = companyService.getCompanyEntityByEmail(companyEmail).orElseThrow(
                () -> new SystemException("Company with email '" + companyEmail + "' not found")
        );
        if (!coupon.getCompany().getId().equals(company.getId())) {
            throw new SystemException("Coupon with id: " + id + " does not belong to company: " + company.getName());

        }
        var category = categoryService.getCategoryEntity(couponRequest.getCategoryId()).orElseThrow(
                () -> new SystemException("Not found category with id: " + couponRequest.getCategoryId()));
        var updatedCoupon = mapToCoupon(couponRequest);
        updatedCoupon.setId(coupon.getId());
        updatedCoupon.setCategory(category);
        updatedCoupon.setCompany(company);
        return Optional.of(mapToCouponResponse(couponRepository.save(updatedCoupon)));

    }

    @Override
    public Optional<CouponResponse> deleteCoupon(Long id) {
        var coupon = couponRepository.findById(id).orElseThrow(
                () -> new SystemException("Not found coupon with id: " + id));
        deleteAllFromCustomers(id);
        couponRepository.delete(coupon);
        return Optional.of(mapToCouponResponse(coupon));
    }

    @Override
    public void updateCouponAmount(Long couponId, Integer amount) {
        try{
            couponRepository.updateCouponAmount(couponId, amount);
        } catch (Exception e) {
            throw new SystemException("Failed to update coupon amount");
        }
    }

    @Override
    public void deleteExpiredCoupons() {
        var coupons = couponRepository.findAllByEndDateBefore(LocalDate.now());
        if (coupons.isEmpty()) {
            return;
        }
        coupons.get().forEach(coupon -> deleteCoupon(coupon.getId()));
    }

    @Override
    public void deleteAllByCompanyId(Long id) {
        var coupons = couponRepository.findAllByCompanyId(id);
        if (coupons.isEmpty()) {
            throw new SystemException("Not found coupons for company with id: " + id);
        }
        coupons.get().forEach(coupon -> deleteCoupon(coupon.getId()));
    }

    @Override
    public void deleteAllByCategory(Long categoryId) {
        var coupons = couponRepository.findAllByCategoryId(categoryId);
        if (coupons.isEmpty()) {
            throw new SystemException("Not found coupons for category with id: " + categoryId);
        }
        coupons.get().forEach(coupon -> deleteCoupon(coupon.getId()));

    }

    @Override
    public void deleteAllCustomerCouponsByCustomerId(Long customerId) {
        var coupons = couponRepository.findAllByCustomerId(customerId);
        if (coupons.isEmpty()) {
            throw new SystemException("Not found coupons for customer with id: " + customerId);
        }
        coupons.get().forEach(coupon -> deleteCouponFromCustomer(coupon.getId(), customerId, coupon.getAmount()));
    }

    @Override
    public void deleteAllFromCustomers(Long couponId) {
        couponRepository.deleteAllCustomersCouponsByCouponId(couponId);
    }

    @Override
    public void addCouponToCustomer(Long couponId, Long CustomerId) {
        couponRepository.addCouponToCustomer(couponId, CustomerId);
    }

    @Override
    public void deleteCouponFromCustomer(Long couponId, Long customerId, Integer amount) {
        couponRepository.deleteCouponFromCustomer(couponId, customerId, amount);
    }


    @Override
    public Boolean existsForCompany(String title, Long companyId) {
        return couponRepository.existsByTitleAndCompanyId(title, companyId);
    }

    @Override
    public List<CouponResponse> getAllCoupons() {
        return couponRepository.findAll().stream().map(this::mapToCouponResponse).collect(Collectors.toList());
    }

    @Override
    public List<CouponResponse> getAllCouponsByTitle(String title) {
        var coupons = couponRepository.findAllByTitle(title).orElseThrow(
                () -> new SystemException("No coupons found for title '" + title + "'")
        );
        return coupons.stream().map(this::mapToCouponResponse).collect(Collectors.toList());
    }

    @Override
    public List<CouponResponse> getAllCouponsByCategory(String categoryName) {
        long id = categoryService.getIdByName(categoryName).orElseThrow(() -> new SystemException("Category '" + categoryName + "' not found"));
        var coupons = couponRepository.findAllByCategoryId(id).orElseThrow(
                () -> new SystemException("No coupons found for category '" + categoryName + "'")
        );
        return coupons.stream().map(this::mapToCouponResponse).collect(Collectors.toList());
    }

    @Override
    public List<CouponResponse> getAllCouponsByCompany(String companyEmail) {
        long companyId = companyService.getIdByEmail(companyEmail).orElseThrow(() -> new SystemException("Company with email '" + companyEmail + "' not found"));
        var coupons = couponRepository.findAllByCompanyId(companyId)
                .orElseThrow(() -> new SystemException("no coupons found for company with id '" + companyId + "'"));
        return coupons.stream().map(this::mapToCouponResponse).collect(Collectors.toList());
    }

    @Override
    public Coupon mapToCoupon(CouponRequest couponRequest) {
        return Coupon.builder()
                .title(couponRequest.getTitle())
                .description(couponRequest.getDescription())
                .startDate(LocalDate.parse(couponRequest.getStartDate()))
                .endDate(LocalDate.parse(couponRequest.getEndDate()))
                .amount(couponRequest.getAmount())
                .price(couponRequest.getPrice())
                .image(couponRequest.getImage())
                .build();
    }

    @Override
    public Coupon mapToCoupon(CouponResponse couponResponse) {
        return Coupon.builder()
                .id(couponResponse.getId())
                .title(couponResponse.getTitle())
                .description(couponResponse.getDescription())
                .startDate(couponResponse.getStartDate())
                .endDate(couponResponse.getEndDate())
                .amount(couponResponse.getAmount())
                .price(couponResponse.getPrice())
                .image(couponResponse.getImage())
                .build();
    }

    @Override
    public CouponResponse mapToCouponResponse(Coupon coupon) {
        return CouponResponse.builder()
                .id(coupon.getId())
                .title(coupon.getTitle())
                .description(coupon.getDescription())
                .startDate(coupon.getStartDate())
                .endDate(coupon.getEndDate())
                .amount(coupon.getAmount())
                .price(coupon.getPrice())
                .image(coupon.getImage())
                .build();
    }
}
