package com.maorzehavi.couponSystem.service.impl;

import com.maorzehavi.couponSystem.model.ClientType;
import com.maorzehavi.couponSystem.model.dto.request.UserRequest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class UserServiceImplTest {
    @Autowired
    private UserServiceImpl userService;

    @Test
    void createUser() {
        userService.createUser(UserRequest.builder()
                .email("root@root.com")
                .password("root")
                .build(), ClientType.ADMINISTRATOR);
    }
}