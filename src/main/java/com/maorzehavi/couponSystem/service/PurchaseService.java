package com.maorzehavi.couponSystem.service;

import com.maorzehavi.couponSystem.model.dto.request.PurchaseRequest;
import com.maorzehavi.couponSystem.model.dto.response.PurchaseResponse;
import com.maorzehavi.couponSystem.model.entity.Purchase;

import java.util.List;
import java.util.Optional;

public interface PurchaseService {

    Optional<PurchaseResponse> getPurchase(Long id);

    Optional<PurchaseResponse> createPurchase(PurchaseRequest purchaseRequest,String customerEmail);

    Optional<Boolean> deletePurchase(Long id);

    List<PurchaseResponse> getAllPurchases();

    List<PurchaseResponse> getAllPurchasesByCustomerId(Long id);

    List<PurchaseResponse> getAllPurchasesByCouponId(Long id);

    List<PurchaseResponse> getAllPurchasesByCompanyId(Long id);


    PurchaseResponse mapToPurchaseResponse(Purchase purchase);


}
