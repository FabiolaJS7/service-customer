package com.bootcamp.service_customer.service;

import com.bootcamp.service_customer.model.CustomerRequest;
import com.bootcamp.service_customer.model.CustomerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {
    Flux<CustomerResponse> getCustomers();
    Mono<CustomerResponse> createCustomer(Mono<CustomerRequest> customerRequest);
    Mono<CustomerResponse> updateCustomer(Mono<CustomerRequest> customerRequest);
    Mono<Boolean> deleteCustomer(Mono<CustomerRequest> customerRequest);


}
