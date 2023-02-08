package com.maorzehavi.couponSystem.service.impl;

import com.maorzehavi.couponSystem.exception.SystemException;
import com.maorzehavi.couponSystem.model.dto.request.PurchaseRequest;
import com.maorzehavi.couponSystem.model.dto.response.PurchaseResponse;
import com.maorzehavi.couponSystem.model.entity.Coupon;
import com.maorzehavi.couponSystem.model.entity.Purchase;
import com.maorzehavi.couponSystem.repository.PurchaseRepository;
import com.maorzehavi.couponSystem.service.CouponService;
import com.maorzehavi.couponSystem.service.CustomerService;
import com.maorzehavi.couponSystem.service.PurchaseService;
import jakarta.validation.Valid;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;

    private final CustomerService customerService;

    private final CouponService couponService;

    public PurchaseServiceImpl(PurchaseRepository purchaseRepository,
                               @Lazy CustomerService customerService,
                               @Lazy CouponService couponService) {
        this.purchaseRepository = purchaseRepository;
        this.customerService = customerService;
        this.couponService = couponService;
    }

    @Override
    public Optional<PurchaseResponse> getPurchase(Long id) {
        return purchaseRepository.findById(id)
                .map(this::mapToPurchaseResponse);
    }

    @Override
    public Optional<PurchaseResponse> createPurchase(@Valid PurchaseRequest purchaseRequest,String customerEmail) {
        var customer = customerService.getCustomerEntityByEmail(customerEmail).orElseThrow();
        var coupons = purchaseRequest.getCouponsIds().stream().map(id ->
                couponService.getCouponEntity(id).orElseThrow()
        ).toList();
        double totalPrice = coupons.stream().mapToDouble(Coupon::getPrice).sum();
        int amount = coupons.size();
        coupons.forEach(coupon -> {
            couponService.addCouponToCustomer(coupon.getId(), customer.getId());
            couponService.updateCouponAmount(coupon.getId(),  - 1);
        });
        var purchase = Purchase.builder()
                .totalPrice(totalPrice)
                .amount(amount)
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .customer(customer)
                .coupons(coupons)
                .build();
        return Optional.of(mapToPurchaseResponse(purchaseRepository.save(purchase)));
    }

    @Override
    public Optional<Boolean> deletePurchase(Long id) {
        return Optional.empty();
    }

    @Override
    public List<PurchaseResponse> getAllPurchases() {
        return purchaseRepository.findAll().stream()
                .map(this::mapToPurchaseResponse)
                .toList();
    }

    @Override
    public List<PurchaseResponse> getAllPurchasesByCustomerId(Long id) {
        return purchaseRepository.findAllByCustomerId(id).orElseThrow(
                        () -> new SystemException("No purchases found for customer id: " + id)
                )
                .stream().map(this::mapToPurchaseResponse)
                .toList();
    }

    @Override
    public List<PurchaseResponse> getAllPurchasesByCouponId(Long id) {
        return purchaseRepository.findAllByCouponId(id).orElseThrow(
                        () -> new SystemException("No purchases found for coupon id: " + id)
                )
                .stream().map(this::mapToPurchaseResponse)
                .toList();
    }

    @Override
    public List<PurchaseResponse> getAllPurchasesByCompanyId(Long id) {
        return purchaseRepository.findAllByCompanyId(id).orElseThrow(
                        () -> new SystemException("No purchases found for company id: " + id)
                )
                .stream().map(this::mapToPurchaseResponse)
                .toList();
    }

    @Override
    public PurchaseResponse mapToPurchaseResponse(Purchase purchase) {
        var coupons = purchase.getCoupons().stream().map(couponService::mapToCouponResponse).toList();
        var customer = customerService.mapToCustomerResponse(purchase.getCustomer());
        return PurchaseResponse.builder()
                .timestamp(purchase.getTimestamp())
                .totalPrice(purchase.getTotalPrice())
                .amount(purchase.getAmount())
                .coupons(coupons)
                .customer(customer)
                .build();
    }
}
