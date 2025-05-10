package com.bootcamp.service_customer.transfer;

import com.bootcamp.service_customer.model.CustomerRequest;
import com.bootcamp.service_customer.model.Customer;

import org.springframework.stereotype.Component;


@Component
public class CustomerTransfer {

    public void getCustomerOfCustomerRequestToUpdate(Customer customerFounded, CustomerRequest customerRq) {
        customerFounded.setName(customerRq.getName());
        customerFounded.setLastName(customerRq.getLastName());
        customerFounded.setEmail(customerRq.getEmail());
        customerFounded.setPhone(customerRq.getPhone());
        customerFounded.setTypeClient(customerRq.getTypeClient());
    }
}
