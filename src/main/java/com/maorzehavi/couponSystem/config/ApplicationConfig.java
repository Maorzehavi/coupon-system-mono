package com.maorzehavi.couponSystem.config;

import com.maorzehavi.couponSystem.job.ExpiredCouponRemover;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ApplicationConfig {

    private final ExpiredCouponRemover expiredCouponRemover;
    @Qualifier("userDetailsServiceImpl")
    private final UserDetailsService userDetailsService;

    public ApplicationConfig(ExpiredCouponRemover expiredCouponRemover,
                             @Lazy UserDetailsService userDetailsService) {
        this.expiredCouponRemover = expiredCouponRemover;
        this.userDetailsService = userDetailsService;
    }

    @PostConstruct
    private void init() {
        expiredCouponRemover.removeExpiredCoupons();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
