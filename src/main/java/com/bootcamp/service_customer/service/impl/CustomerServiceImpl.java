package com.bootcamp.service_customer.service.impl;

import com.bootcamp.service_customer.constants.StatusConstants;
import com.bootcamp.service_customer.mapper.CustomerMapper;
import com.bootcamp.service_customer.model.CustomerRequest;
import com.bootcamp.service_customer.model.CustomerResponse;
import com.bootcamp.service_customer.model.entity.AuditData;
import com.bootcamp.service_customer.model.entity.Customer;
import com.bootcamp.service_customer.repository.CustomerRepository;
import com.bootcamp.service_customer.service.CustomerService;
import com.bootcamp.service_customer.util.AuditDataUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
@AllArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    CustomerRepository customerRepository;
    CustomerMapper customerMapper;
    AuditDataUtil auditDataUtil;

    @Override
    public Flux<CustomerResponse> getCustomers() {
        Flux<Customer> customerResponseFlux = customerRepository.findAll();
        return customerResponseFlux
                .map(customer -> customerMapper.getCustomerResponseOfCustomer(customer));
    }

    @Override
    public Mono<CustomerResponse> createCustomer(Mono<CustomerRequest> customerRequest) {
        return customerRequest
                .flatMap(customerRq -> {
                    Customer customer = customerMapper.getCustomerOfCustomerRequest(customerRq);
                    customer.setAuditData(auditDataUtil.create(customerRq.getCreatedBy()));
                    return customerRepository.save(customer);
                }).map(customer -> customerMapper.getCustomerResponseOfCustomer(customer));
    }

    @Override
    public Mono<CustomerResponse> updateCustomer(String customerId, Mono<CustomerRequest> customerRequest) {
        return customerRepository.findById(customerId)
                .flatMap(customerFounded ->
                        customerRequest
                                .flatMap(customerRq -> {
                                    customerMapper.getCustomerOfCustomerRequestToUpdate(customerFounded, customerRq);
                                    auditDataUtil.update(customerFounded.getAuditData(), customerRq.getCreatedBy());
                                    return customerRepository.save(customerFounded);
                                })
                )
                .map(updatedCustomer -> customerMapper.getCustomerResponseOfCustomer(updatedCustomer))
                .switchIfEmpty(Mono.error(new RuntimeException("Customer not found")));
    }

    @Override
    public Mono<Boolean> deleteCustomer(Mono<CustomerRequest> customerRequest) {
        return customerRequest
                .flatMap(request -> customerRepository.findById(request.getId())
                        .flatMap(customerFounded -> {
                            customerFounded.setStatus(StatusConstants.INACTIVE);
                            return customerRepository.save(customerFounded);
                        }))
                .hasElement();
    }

    @Override
    public Mono<CustomerResponse> findCustomerById(String customerId) {
        return customerRepository.findById(customerId)
                .map(customer -> customerMapper.getCustomerResponseOfCustomer(customer));
    }
}
