package com.bootcamp.service_customer.model;

import com.bootcamp.service_customer.constants.StatusConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "customers")
public class Customer {

    @Id
    private String id;
    private String name;
    private String lastName;
    private String phone;
    private String email;
    private String status = StatusConstants.ACTIVE;
    private String typeClient;
    private Identification identification;
    private AuditData auditData;

}
