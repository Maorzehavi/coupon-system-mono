package com.maorzehavi.couponSystem.job;

import com.maorzehavi.couponSystem.service.CouponService;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ExpiredCouponRemover {

    private final CouponService couponService;

    public ExpiredCouponRemover(
            @Lazy CouponService couponService) {
        this.couponService = couponService;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void removeExpiredCoupons() {
        couponService.deleteExpiredCoupons();
    }
}
