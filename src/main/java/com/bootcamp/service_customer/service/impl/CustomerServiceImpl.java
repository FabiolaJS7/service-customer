package com.bootcamp.service_customer.service.impl;

import com.bootcamp.service_customer.constants.StatusConstants;
import com.bootcamp.service_customer.mapper.CustomerMapper;
import com.bootcamp.service_customer.model.CustomerRequest;
import com.bootcamp.service_customer.model.CustomerResponse;
import com.bootcamp.service_customer.model.entity.Customer;
import com.bootcamp.service_customer.repository.CustomerRepository;
import com.bootcamp.service_customer.service.CustomerService;
import com.bootcamp.service_customer.util.AuditDataUtil;
import com.bootcamp.service_customer.util.JsonTransferUtil;
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
    AuditDataUtil auditDataUtil;

    @Override
    public Flux<CustomerResponse> getCustomers() {
        return customerRepository.findAll()
                .doOnSubscribe(subscription -> log.info("Getting all customers from the database"))
                .map(customer -> customerMapper.getCustomerResponseOfCustomer(customer))
                .doOnComplete(() -> log.info("Completed fetching and mapping all customers"))
                .doOnError(e -> log.error("Error occurred while getting customers: {}", e.getMessage(), e));
    }

    @Override
    public Mono<CustomerResponse> createCustomer(Mono<CustomerRequest> customerRequest) {
        return customerRequest
                .doOnNext(customerRq -> log.info("Received CustomerRequest: {}", customerRq))
                .flatMap(customerRq -> {
                    log.info("Mapping CustomerRequest to Customer entity");
                    Customer customer = customerMapper.getCustomerOfCustomerRequest(customerRq);
                    customer.setAuditData(auditDataUtil.create(customerRq.getCreatedBy()));
                    log.info("Saving Customer entity");
                    return customerRepository.save(customer);
                })
                .doOnSuccess(savedCustomer -> log.info("Customer saved successfully: {}", savedCustomer.getId()))
                .map(customer -> customerMapper.getCustomerResponseOfCustomer(customer))
                .doOnError(e -> log.error("Error occurred while creating customer", e));
    }

    @Override
    public Mono<CustomerResponse> updateCustomer(String customerId, Mono<CustomerRequest> customerRequest) {
        return customerRepository.findById(customerId)
                .doOnNext(customerRq -> log.info("Received CustomerRequest."))
                .flatMap(customerFounded ->
                        customerRequest
                                .flatMap(customerRq -> {
                                    log.info("Setting information to update of CustomerRequest to Customer entity");
                                    customerMapper.getCustomerOfCustomerRequestToUpdate(customerFounded, customerRq);
                                    auditDataUtil.update(customerFounded.getAuditData(), customerRq.getCreatedBy());
                                    log.info("Saving Customer entity: {}", customerRq);
                                    return customerRepository.save(customerFounded);
                                })
                )
                .doOnSuccess(savedCustomer -> log.info("Customer updated successfully: {}", savedCustomer.getId()))
                .map(updatedCustomer -> customerMapper.getCustomerResponseOfCustomer(updatedCustomer))
                .doOnError(e -> log.error("Error occurred while updating customer with ID {}: {}", customerId, e.getMessage(), e))
                .switchIfEmpty(Mono.error(new RuntimeException("Customer not found")));
    }

    @Override
    public Mono<Boolean> deleteCustomer(String customerId) {
        return customerRepository.findById(customerId)
                .doOnNext(customer -> log.info("Customer found with ID {}", customerId))
                .flatMap(customerFounded ->  {
                    customerFounded.setStatus(StatusConstants.INACTIVE);
                    auditDataUtil.update(customerFounded.getAuditData(), customerFounded.getAuditData().getCreatedBy());
                    log.info("Deleting customer : {}", JsonTransferUtil.objectToJson(customerFounded));
                    return customerRepository.save(customerFounded);
                })
                .doOnSuccess(savedCustomer -> log.info("Customer successfully updated to delete with ID: {}", customerId))
                .hasElement()
                .doOnNext(deleted -> {
                        if (deleted) {
                            log.info("Customer with ID {} was successfully marked as INACTIVE", customerId);
                        } else {
                            log.warn("Customer with ID {} was not found", customerId);
                        }
                })
                .doOnError(e -> log.error("Error occurred while deleting customer with ID {}: {}", customerId, e.getMessage(), e))
                .switchIfEmpty(Mono.error(new RuntimeException("Customer not found")));
    }

    @Override
    public Mono<CustomerResponse> findCustomerById(String customerId) {
        return customerRepository.findById(customerId)
                .map(customer -> customerMapper.getCustomerResponseOfCustomer(customer));
    }
}
