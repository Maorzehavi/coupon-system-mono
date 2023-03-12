package com.maorzehavi.couponSystem.service.impl;

import com.maorzehavi.couponSystem.exception.SystemException;
import com.maorzehavi.couponSystem.model.ClientType;
import com.maorzehavi.couponSystem.model.dto.request.ClientRequest;
import com.maorzehavi.couponSystem.model.dto.request.CustomerRequest;
import com.maorzehavi.couponSystem.model.dto.response.CustomerResponse;
import com.maorzehavi.couponSystem.model.entity.Customer;
import com.maorzehavi.couponSystem.repository.CustomerRepository;
import com.maorzehavi.couponSystem.service.CouponService;
import com.maorzehavi.couponSystem.service.CustomerService;
import com.maorzehavi.couponSystem.service.EmailService;
import com.maorzehavi.couponSystem.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    @Qualifier("userServiceImpl")
    private final UserService userService;
    @Qualifier("couponServiceImpl")
    private final CouponService couponService;
    @Qualifier("emailServiceImpl")
    private final EmailService emailService;

    public CustomerServiceImpl(CustomerRepository customerRepository,
                               @Lazy UserService userService,
                               @Lazy CouponService couponService,
                               @Lazy EmailService emailService) {
        this.customerRepository = customerRepository;
        this.userService = userService;
        this.couponService = couponService;
        this.emailService = emailService;
    }

    @Override
    public Optional<Long> getIdByEmail(String email) {
        return customerRepository.getIdByEmail(email);
    }

    @Override
    public Optional<CustomerResponse> getCustomer(Long id) {
        return customerRepository.findById(id).map(this::mapToCustomerResponse);
    }

    @Override
    public Optional<Customer> getCustomerEntity(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public Optional<Customer> getCustomerEntityByEmail(String email) {
        return customerRepository.getCustomerByEmail(email);
    }

    @Override
    public Optional<CustomerResponse> createCustomer(@Valid ClientRequest<CustomerRequest> customerRequest) {
        var customer = mapToCustomer(customerRequest.getData());
        var user = userService.createUser(customerRequest.getUser(), ClientType.CUSTOMER).map(userService::mapToUser)
                .orElseThrow(() -> new SystemException("Failed to create user"));
        customer.setUser(user);
        var customerResponse = Optional.of(mapToCustomerResponse(customerRepository.save(customer)));
        emailService.sendEmail(customerResponse.get().getEmail(), "Welcome to Coupon System",
                "Hello " + customerResponse.get().getFirstName() + " " + customerResponse.get().getLastName() + ",\n" +
                        "Welcome to Coupon System, we are happy to have you as a customer.\n" +
                        "You can now start purchasing coupons and enjoy the benefits of our system.\n" +
                        "Best regards,\n" +
                        "Coupon System Team");
        return customerResponse;


    }

    @Override
    public Optional<CustomerResponse> updateCustomer(@Valid ClientRequest<CustomerRequest> customerRequest,
                                                     Principal principal) {
        var user = userService.updateUser(customerRequest.getUser(), principal).map(userService::mapToUser)
                .orElseThrow(() -> new SystemException("Failed to update user"));
        var customer = getCustomerEntityByEmail(principal.getName()).orElseThrow();
        customer.setFirstName(customerRequest.getData().getFirstName());
        customer.setLastName(customerRequest.getData().getLastName());
        customer.setUser(user);
        return Optional.of(mapToCustomerResponse(customerRepository.save(customer)));
    }

    @Override
    public Optional<CustomerResponse> deleteCustomer(Long id) {
        var customer = getCustomerEntity(id).orElseThrow();
        couponService.deleteAllCustomerCouponsByCustomerId(id);
        userService.deleteUser(customer.getUser().getId());
        customerRepository.delete(customer);
        emailService.sendEmail(customer.getUser().getEmail(), "Goodbye from Coupon System",
                "Hello " + customer.getFirstName() + " " + customer.getLastName() + ",\n" +
                        "We are sorry to see you go, we hope you enjoyed your time with us.\n" +
                        "Best regards,\n" +
                        "Coupon System Team");
        return Optional.of(mapToCustomerResponse(customer));
    }

    @Override
    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll().stream().map(this::mapToCustomerResponse).toList();
    }

    @Override
    public CustomerResponse mapToCustomerResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getUser().getEmail())
                .build();
    }

    @Override
    public Customer mapToCustomer(CustomerRequest customerRequest) {
        return Customer.builder()
                .firstName(customerRequest.getFirstName())
                .lastName(customerRequest.getLastName())
                .build();
    }
}
