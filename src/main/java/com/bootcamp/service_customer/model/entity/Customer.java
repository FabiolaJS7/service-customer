package com.bootcamp.service_customer.model.entity;

import com.bootcamp.service_customer.constants.StatusConstants;
import lombok.Data;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collation = "customers")
public class Customer {

    @Id
    private String id;
    private String name;
    private String lastName;
    private String phone;
    private String email;
    private String status = StatusConstants.ACTIVE;
    private Date createdAt;
    private Date updatedAt;

}
