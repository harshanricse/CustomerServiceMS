package com.harsha.infytel_customer.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Plan {
    @Id
    private Integer planId;
    private String planName;
    private Integer nationalRate;
    private Integer localRate;

}
