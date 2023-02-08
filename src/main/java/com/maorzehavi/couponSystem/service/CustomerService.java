package com.maorzehavi.couponSystem.service;

import com.maorzehavi.couponSystem.model.dto.request.ClientRequest;
import com.maorzehavi.couponSystem.model.dto.request.CustomerRequest;
import com.maorzehavi.couponSystem.model.dto.response.CustomerResponse;
import com.maorzehavi.couponSystem.model.entity.Customer;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

public interface CustomerService {

    Optional<Long> getIdByEmail(String email);

    Optional<CustomerResponse> getCustomer(Long id);

    Optional<Customer> getCustomerEntity(Long id);

    Optional<Customer> getCustomerEntityByEmail(String email);

    @Modifying
    @Transactional
    Optional<CustomerResponse> createCustomer(ClientRequest<CustomerRequest> customerRequest);

    @Modifying
    @Transactional
    Optional<CustomerResponse> updateCustomer(ClientRequest<CustomerRequest> customerRequest, Principal principal);

    @Modifying
    @Transactional
    Optional<CustomerResponse> deleteCustomer(Long id);

    List<CustomerResponse> getAllCustomers();

    CustomerResponse mapToCustomerResponse(Customer customer);

    Customer mapToCustomer(CustomerRequest customerRequest);

}
