package com.bootcamp.service_customer.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuditData {

    private Date createdAt;
    private Date updatedAt;
    private String createdBy;
    private String updatedBy;
}
