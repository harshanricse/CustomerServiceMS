package com.harsha.infytel_customer.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Customer {
    @Id
    private long phoneNo;
    private String name;
    private Integer age;
    private String address;
    private String password;
    private char gender;
    private Integer planId;
}
