package com.harsha.infytel_customer.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class FriendFamily {
    @Id
    @GeneratedValue
    private long id;
    private long phoneNo;
    private long friendAndFamily;

}
