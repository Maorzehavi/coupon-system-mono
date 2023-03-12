package com.maorzehavi.couponSystem;

import com.maorzehavi.couponSystem.model.ClientType;
import com.maorzehavi.couponSystem.model.dto.request.AuthorityRequest;
import com.maorzehavi.couponSystem.model.dto.request.RoleRequest;
import com.maorzehavi.couponSystem.model.dto.request.UserRequest;
import com.maorzehavi.couponSystem.service.RoleService;
import com.maorzehavi.couponSystem.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Set;

@SpringBootApplication
public class CouponSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(CouponSystemApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(RoleService roleService, UserService userService) {
    // TODO: remove this method
        return (args) -> {
            roleService.createRole(RoleRequest.builder()
                    .role("admin")
                    .clientType(ClientType.ADMINISTRATOR)
                    .authorities(Set.of(
                            AuthorityRequest.builder()
                                    .authority("crud:user")
                                    .build(), AuthorityRequest.builder()
                                    .authority("crud:customer")
                                    .build(),
                            AuthorityRequest.builder()
                                    .authority("crud:company")
                                    .build()
                    )).build());
            roleService.createRole(RoleRequest.builder()
                    .role("company")
                    .clientType(ClientType.COMPANY)
                    .authorities(Set.of(
                            AuthorityRequest.builder()
                                    .authority("crud:coupon")
                                    .build()
                    )).build());
            roleService.createRole(RoleRequest.builder()
                    .role("customer")
                    .clientType(ClientType.CUSTOMER)
                    .authorities(Set.of(
                            AuthorityRequest.builder()
                                    .authority("get:coupon")
                                    .build()
                    )).build());
            UserRequest userRequest = new UserRequest();
            userRequest.setEmail("admin@admin.com");
            userRequest.setPassword("admin");
            userService.createUser(userRequest, ClientType.ADMINISTRATOR);
        };
    }
}
