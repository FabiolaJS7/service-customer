package com.bootcamp.service_customer;



import com.bootcamp.service_customer.api.ApiApiDelegate;
import com.bootcamp.service_customer.model.CustomerRequest;
import com.bootcamp.service_customer.model.CustomerResponse;
import com.bootcamp.service_customer.service.CustomerService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class CustomerDelegateImpl implements ApiApiDelegate {

    @Autowired
    CustomerService customerService;


    @Override
    public Mono<ResponseEntity<CustomerResponse>> createCustomer(Mono<CustomerRequest> customerRequest, ServerWebExchange exchange) {
        return customerService.createCustomer(customerRequest)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    return Mono.just(ResponseEntity.status(500).build());
                });
    }

    @Override
    public Mono<ResponseEntity<Flux<CustomerResponse>>> findAll(ServerWebExchange exchange) {
        return Mono.just(ResponseEntity.ok(customerService.getCustomers()));
    }


}
