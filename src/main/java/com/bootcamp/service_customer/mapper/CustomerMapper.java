package com.bootcamp.service_customer.mapper;

import com.bootcamp.service_customer.model.CustomerRequest;
import com.bootcamp.service_customer.model.CustomerResponse;
import com.bootcamp.service_customer.model.entity.Customer;

import org.springframework.stereotype.Component;


@Component
public class CustomerMapper {

    public CustomerResponse getCustomerResponseOfCustomer(Customer customer) {
        CustomerResponse response = new CustomerResponse();
        response.setId(customer.getId());
        response.setName(customer.getName());
        response.setLastName(customer.getLastName());
        response.setEmail(customer.getEmail());
        response.setPhone(customer.getPhone());
        response.setTypeClient(customer.getTypeClient());
        return response;
    }

    public Customer getCustomerOfCustomerRequest(CustomerRequest customerRequest) {
        Customer customer = new Customer();
        customer.setId(customerRequest.getId());
        customer.setName(customerRequest.getName());
        customer.setLastName(customerRequest.getLastName());
        customer.setEmail(customerRequest.getEmail());
        customer.setPhone(customerRequest.getPhone());
        customer.setTypeClient(customerRequest.getTypeClient());
        return customer;
    }
}
