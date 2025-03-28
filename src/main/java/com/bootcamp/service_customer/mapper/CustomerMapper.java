package com.bootcamp.service_customer.mapper;

import com.bootcamp.service_customer.model.entity.Customer;
import com.bootcamp.servicecustomer.dto.CustomerRequest;
import com.bootcamp.servicecustomer.dto.CustomerResponse;
import org.springframework.stereotype.Component;


@Component
public class CustomerMapper {

    public CustomerResponse getCustomerResponseOfCustomer(Customer customer) {
        CustomerResponse response = new CustomerResponse();
        customer.setId(customer.getId());
        customer.setName(customer.getName());
        customer.setLastName(customer.getLastName());
        customer.setEmail(customer.getEmail());
        customer.setPhone(customer.getPhone());
        return response;
    }

    public Customer getCustomerOfCustomerRequest(CustomerRequest customerRequest) {
        Customer customer = new Customer();
        customer.setId(customerRequest.getId());
        customer.setName(customerRequest.getName());
        customer.setLastName(customerRequest.getLastName());
        customer.setEmail(customerRequest.getEmail());
        customer.setPhone(customerRequest.getPhone());
        return customer;
    }
}
