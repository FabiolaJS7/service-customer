package com.bootcamp.service_customer.expose;

import com.bootcamp.service_customer.api.ApiApiDelegate;
import com.bootcamp.service_customer.model.CustomerRequest;
import com.bootcamp.service_customer.model.CustomerResponse;
import com.bootcamp.service_customer.service.CustomerService;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@AllArgsConstructor
public class CustomerDelegateImpl implements ApiApiDelegate {

    CustomerService customerService;


    @Override
    public Mono<ResponseEntity<CustomerResponse>> createCustomer(Mono<CustomerRequest> customerRequest,
                                                                 ServerWebExchange exchange) {
        log.info("Customer create");
        return customerService.createCustomer(customerRequest)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.status(404).build()));
    }

    @Override
    public Mono<ResponseEntity<Flux<CustomerResponse>>> findAll(ServerWebExchange exchange) {
        log.info("-> Customer findAll");
        return Mono.just(ResponseEntity.ok(customerService.getCustomers()));
    }

    @Override
    public Mono<ResponseEntity<CustomerResponse>> update(String customerId, Mono<CustomerRequest> customerRequest,
                                                         ServerWebExchange exchange) {
        log.info("-> Customer update");
        return customerService.updateCustomer(customerId, customerRequest)
                .flatMap(customerResponse -> {
                    if (customerResponse.getId() == null) {
                        log.warn("Customer with ID {} not found", customerId);
                        return Mono.just(ResponseEntity.status(404).body(new CustomerResponse()));
                    }
                    return Mono.just(ResponseEntity.ok(customerResponse));
                })
                .onErrorResume(e -> {
                    log.error("Error occurred while updating customer with ID {}: {}", customerId, e.getMessage(), e);
                    return Mono.just(ResponseEntity.status(500).body(null));
                });
    }

    @Override
    public Mono<ResponseEntity<Void>> delete(String customerId, ServerWebExchange exchange) {
        log.info("-> Customer delete");
        return customerService.deleteCustomer(customerId)
                .flatMap(deleted -> {
                    if (Boolean.TRUE.equals(deleted)) {
                        return Mono.just(ResponseEntity.ok().build());
                    } else {
                        return Mono.just(new ResponseEntity<>(HttpStatus.NOT_FOUND));
                    }
                });
    }

    @Override
    public Mono<ResponseEntity<CustomerResponse>> getById(String customerId, ServerWebExchange exchange) {
        log.info("-> Customer findById");
        return customerService.getCustomerById(customerId)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(new ResponseEntity<>(HttpStatus.NOT_FOUND)));
    }

}
