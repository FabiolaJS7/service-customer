package com.bootcamp.service_customer.service.impl;

import com.bootcamp.service_customer.constants.StatusConstants;
import com.bootcamp.service_customer.mapper.CustomerMapper;
import com.bootcamp.service_customer.repository.CustomerRepository;
import com.bootcamp.service_customer.service.CustomerService;
import com.bootcamp.servicecustomer.dto.CustomerRequest;
import com.bootcamp.servicecustomer.dto.CustomerResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    CustomerRepository customerRepository;
    CustomerMapper customerMapper;

    @Override
    public Flux<CustomerResponse> getCustomers() {
        return customerRepository.findAll()
                .map(customer -> customerMapper.getCustomerResponseOfCustomer(customer));
    }

    @Override
    public Mono<CustomerResponse> createCustomer(CustomerRequest customerRequest) {
        return customerRepository.save(customerMapper.getCustomerOfCustomerRequest(customerRequest))
                .map(c -> customerMapper.getCustomerResponseOfCustomer(c));
    }

    @Override
    public Mono<CustomerResponse> updateCustomer(CustomerRequest customerRequest) {
        return customerRepository.findById(customerRequest.getId()).flatMap(c -> {
                    c.setName(customerRequest.getName());
                    c.setLastName(customerRequest.getLastName());
                    c.setEmail(customerRequest.getEmail());
                    c.setPhone(customerRequest.getPhone());
                    return customerRepository.save(c);
                })
                .map(c -> customerMapper.getCustomerResponseOfCustomer(c));
    }

    @Override
    public Mono<Boolean> deleteCustomer(CustomerRequest customerRequest) {
        return customerRepository.findById(customerRequest.getId()).flatMap(c -> {
                    c.setStatus(StatusConstants.INACTIVE);
                    return customerRepository.save(c);
                }).map(c -> customerMapper.getCustomerResponseOfCustomer(c))
                .hasElement();
    }
}
