package com.maorzehavi.couponSystem;

import com.maorzehavi.couponSystem.model.ClientType;
import com.maorzehavi.couponSystem.model.dto.request.UserRequest;
import com.maorzehavi.couponSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CouponSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(CouponSystemApplication.class, args);
    }

}
