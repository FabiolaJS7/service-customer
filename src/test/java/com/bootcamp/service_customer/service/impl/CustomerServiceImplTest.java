package com.bootcamp.service_customer.service.impl;

import com.bootcamp.service_customer.mapper.CustomerMapperStruct;
import com.bootcamp.service_customer.model.*;
import com.bootcamp.service_customer.repository.CustomerRepository;
import com.bootcamp.service_customer.util.JsonTransferUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @InjectMocks
    CustomerServiceImpl customerService;

    @Mock
    CustomerRepository customerRepository;

    @Spy
    CustomerMapperStruct customerMapperStruct;

    @Test
    void shouldCustomers_whenGetAllCustomers_thenSuccessOk() {
        //arr
        List<Customer> customers = Arrays.asList(JsonTransferUtil.getObjectFromJSONFile(Customer[].class,
                "customers.json"));

        Mockito.when(customerRepository.findAll()).thenReturn(Flux.fromIterable(customers));

        //Assert
        StepVerifier.create(customerService.getCustomers().map(CustomerResponse::getTypeClient)) //act
                .expectNext("P")
                .thenAwait(Duration.ofSeconds(1))
                .expectNext("B")
                .thenAwait(Duration.ofSeconds(1))
                .expectNext("M")
                .thenAwait(Duration.ofSeconds(1))
                .expectNext("V")
                .expectComplete()
                .verify();
    }

    @Test
    void shouldCustomerResponse_whenCustomerWasCreated_thenSuccessOk() {
        CustomerRequest customerRequest = new CustomerRequest();
        customerRequest.setName("GEORGE");
        customerRequest.setLastName("TARAZONA JIMENEZ");
        customerRequest.setTypeClient("P");
        customerRequest.setPhone("9874563214");
        customerRequest.setEmail("jimenez@gmail.com");
        customerRequest.setCreatedBy("admin@bank.com");
        customerRequest.setIdentificationType("DNI");
        customerRequest.setNumIdentification("4779633");

        Customer customer = new Customer();
        customer.setName("GEORGE");
        customer.setLastName("TARAZONA JIMENEZ");
        customer.setTypeClient("P");
        customer.setPhone("9874563214");
        customer.setEmail("jimenez@gmail.com");
        AuditData auditData = new AuditData();
        auditData.setCreatedBy("admin@bank.com");
        customer.setAuditData(auditData);

        Identification identification = new Identification();
        identification.setType("DNI");
        identification.setValue("4779633");
        customer.setIdentification(identification);

        Mockito.when(customerRepository.save(Mockito.any(Customer.class))).thenReturn(Mono.just(customer));

        //Assert
        StepVerifier.create(customerService.createCustomer(Mono.just(customerRequest))
                        .map(CustomerResponse::getNumIdentification)) //act
                .expectNextMatches(numIdentification -> numIdentification.equals("4779633"))
                .verifyComplete();

    }
}