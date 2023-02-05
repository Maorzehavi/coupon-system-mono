package com.maorzehavi.couponSystem.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseResponse {

    private Timestamp timestamp;

    private Integer amount;

    private Double totalPrice;

    private CustomerResponse customer;

    private List<CouponResponse> coupons;
}
