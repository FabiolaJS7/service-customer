package com.bootcamp.service_customer.service.impl;

import com.bootcamp.service_customer.mapper.CustomerMapperStruct;
import com.bootcamp.service_customer.model.*;
import com.bootcamp.service_customer.repository.CustomerRepository;
import com.bootcamp.service_customer.transfer.CustomerTransfer;
import com.bootcamp.service_customer.util.AuditDataUtil;
import com.bootcamp.service_customer.util.JsonTransferUtil;
import org.junit.jupiter.api.Disabled;
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

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @InjectMocks
    CustomerServiceImpl customerService;

    @Mock
    CustomerRepository customerRepository;

    @Spy
    CustomerMapperStruct customerMapperStruct;

    @Spy
    CustomerTransfer customerTransfer;

    @Spy
    AuditDataUtil auditDataUtil;

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
        CustomerRequest customerRequest = getCustomerRequest();
        Customer customer = getCustomer();

        Mockito.when(customerRepository.save(Mockito.any(Customer.class))).thenReturn(Mono.just(customer));

        //Assert
        StepVerifier.create(customerService.createCustomer(Mono.just(customerRequest))
                        .map(CustomerResponse::getNumIdentification)) //act
                .expectNextMatches(numIdentification -> numIdentification.equals("4779633"))
                .verifyComplete();

    }

    @Test
    void shouldCustomerUpdated_whenCustomerWasUpdated_thenSuccessOk() {
        // Customer request
        CustomerRequest customerRequest = getCustomerRequest();
        // Customer en la base de datos
        Customer customer = getCustomer();

        Mockito.when(customerRepository.findById(customer.getId())).thenReturn(Mono.just(customer));
        Mockito.when(customerRepository.save(Mockito.any(Customer.class))).thenReturn(Mono.just(customer));

        //Assert
        StepVerifier.create(customerService.updateCustomer(customer.getId(), Mono.just(customerRequest))
                        .map(CustomerResponse::getPhone)) //act
                .expectNextMatches(numIdentification -> numIdentification.equals("77777777"))
                .verifyComplete();

    }

    @Test
    void shouldCustomerResponse_whenCustomerWasDeleted_thenSuccessOk() {
        //Customer de la base de datos
        Customer customer = getCustomer();

        Mockito.when(customerRepository.findById(customer.getId())).thenReturn(Mono.just(customer));
        Mockito.when(customerRepository.save(Mockito.any(Customer.class))).thenReturn(Mono.just(customer));

        //Assert
        StepVerifier.create(customerService.deleteCustomer(customer.getId())) //act
                .expectNext(true)
                .verifyComplete();

    }

    @Test
    void shouldCustomerResponse_whenSendCustomerById_thenSuccessOk() {
        //Customer de la base de datos
        Customer customer = getCustomer();

        Mockito.when(customerRepository.findById(customer.getId())).thenReturn(Mono.just(customer));

        //Assert
        StepVerifier.create(customerService.getCustomerById(customer.getId()).map(CustomerResponse::getId)) //act
                .expectNextMatches(id -> id.equals(customer.getId()))
                .verifyComplete();

    }

    private Customer getCustomer() {
        // Customer en la base de datos
        Customer customer = new Customer();
        customer.setId("ID-AAAAA");
        customer.setName("GEORGE");
        customer.setLastName("TARAZONA JIMENEZ");
        customer.setTypeClient("P");
        customer.setPhone("99999999");
        customer.setEmail("jimenez@gmail.com");
        AuditData auditData = new AuditData();
        auditData.setCreatedBy("admin@bank.com");
        customer.setAuditData(auditData);

        Identification identification = new Identification();
        identification.setType("DNI");
        identification.setValue("4779633");
        customer.setIdentification(identification);
        return customer;
    }

    public CustomerRequest getCustomerRequest() {
        CustomerRequest customerRequest = new CustomerRequest();
        customerRequest.setName("GEORGE");
        customerRequest.setLastName("TARAZONA JIMENEZ");
        customerRequest.setTypeClient("P");
        customerRequest.setPhone("77777777");
        customerRequest.setEmail("jimenez@gmail.com");
        customerRequest.setCreatedBy("admin@bank.com");
        customerRequest.setIdentificationType("DNI");
        customerRequest.setNumIdentification("4779633");
        return customerRequest;
    }
}