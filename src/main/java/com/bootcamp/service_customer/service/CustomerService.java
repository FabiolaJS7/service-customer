package com.bootcamp.service_customer.service;


import com.bootcamp.servicecustomer.dto.CustomerRequest;
import com.bootcamp.servicecustomer.dto.CustomerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {
    Flux<CustomerResponse> getCustomers();
    Mono<CustomerResponse> createCustomer(CustomerRequest customerRequest);
    Mono<CustomerResponse> updateCustomer(CustomerRequest customerRequest);
    Mono<Boolean> deleteCustomer(CustomerRequest customerRequest);


}
