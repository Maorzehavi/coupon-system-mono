package com.maorzehavi.couponSystem.service.impl;

import com.maorzehavi.couponSystem.model.ClientType;
import com.maorzehavi.couponSystem.model.dto.request.AuthorityRequest;
import com.maorzehavi.couponSystem.model.dto.request.RoleRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RoleServiceImplTest {
    private final RoleServiceImpl roleService;

    @Autowired
    RoleServiceImplTest(RoleServiceImpl roleService) {
        this.roleService = roleService;
    }

    @Test
    void createRole() {
        roleService.createRole(RoleRequest.builder()
                .role("admin")
                .authorities(Set.of(
                        AuthorityRequest.builder().authority("delete:company").build()
                ))
                .clientType(ClientType.ADMINISTRATOR)
                .build());
    }

    @Test
    void updateRole() {
        roleService.updateRole(3L,RoleRequest.builder()

                .role("admin")
                .authorities(Set.of(
                        AuthorityRequest.builder().authority("crud:company").build()
                ))
                .clientType(ClientType.ADMINISTRATOR)
                .build());
    }
}