package com.bootcamp.service_customer.mapper;

import com.bootcamp.service_customer.model.Customer;
import com.bootcamp.service_customer.model.CustomerRequest;
import com.bootcamp.service_customer.model.CustomerResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CustomerMapperStruct {

    CustomerMapperStruct INSTANCE = Mappers.getMapper(CustomerMapperStruct.class);

    // Mapea Customer a CustomerResponse
    @Mapping(source = "identification.type", target = "identificationType")
    @Mapping(source = "identification.value", target = "numIdentification")
    CustomerResponse toCustomerResponseOfCustomer(Customer customerModel);

    @Mapping(source = "identificationType", target = "identification.type")
    @Mapping(source = "numIdentification", target = "identification.value")
    Customer toCustomerOfCustomerRequest(CustomerRequest customerRequest);
}
