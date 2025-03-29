package com.bootcamp.service_customer.repository;

import com.bootcamp.service_customer.model.entity.Customer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CustomerRepository extends ReactiveMongoRepository<Customer, String> {
}
