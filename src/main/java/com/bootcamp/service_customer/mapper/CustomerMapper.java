package com.bootcamp.service_customer.mapper;

import com.bootcamp.service_customer.model.CustomerRequest;
import com.bootcamp.service_customer.model.CustomerResponse;
import com.bootcamp.service_customer.model.entity.Customer;

import com.bootcamp.service_customer.model.entity.Identification;
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

        if (customer.getIdentification() != null) {
            response.setIdentificationType(customer.getIdentification().getType());
            response.setNumIdentification(customer.getIdentification().getValue());
        }

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

        Identification identification = new Identification();
        identification.setType(customerRequest.getIdentificationType());
        identification.setValue(customerRequest.getNumIdentification());
        customer.setIdentification(identification);

        return customer;
    }

    public void getCustomerOfCustomerRequestToUpdate(Customer customerFounded, CustomerRequest customerRq) {
        customerFounded.setName(customerRq.getName());
        customerFounded.setLastName(customerRq.getLastName());
        customerFounded.setEmail(customerRq.getEmail());
        customerFounded.setPhone(customerRq.getPhone());
        customerFounded.setTypeClient(customerRq.getTypeClient());
    }
}
