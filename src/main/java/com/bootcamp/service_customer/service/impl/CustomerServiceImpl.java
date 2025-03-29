package com.bootcamp.service_customer.service.impl;

import com.bootcamp.service_customer.constants.StatusConstants;
import com.bootcamp.service_customer.mapper.CustomerMapper;
import com.bootcamp.service_customer.model.CustomerRequest;
import com.bootcamp.service_customer.model.CustomerResponse;
import com.bootcamp.service_customer.repository.CustomerRepository;
import com.bootcamp.service_customer.service.CustomerService;
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
    public Mono<CustomerResponse> createCustomer(Mono<CustomerRequest> customerRequest) {
        return customerRequest
                .map(c -> customerMapper.getCustomerOfCustomerRequest(c))
                .flatMap(customer -> customerRepository.save(customer))
                .map(customer -> customerMapper.getCustomerResponseOfCustomer(customer));
    }

    @Override
    public Mono<CustomerResponse> updateCustomer(Mono<CustomerRequest> customerRequest) {
        return customerRequest
                .flatMap(request -> customerRepository.findById(request.getId())
                        .flatMap(customerFounded -> {
                            customerFounded.setName(request.getName());
                            customerFounded.setLastName(request.getLastName());
                            customerFounded.setEmail(request.getEmail());
                            customerFounded.setPhone(request.getPhone());
                            customerFounded.setStatus(StatusConstants.ACTIVE);
                            customerFounded.setType(request.getTypeClient());
                            return customerRepository.save(customerFounded);
                        })
                )
                .map(updatedCustomer -> customerMapper.getCustomerResponseOfCustomer(updatedCustomer));

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
}
