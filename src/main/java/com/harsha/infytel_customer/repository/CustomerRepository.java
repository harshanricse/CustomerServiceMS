package com.harsha.infytel_customer.repository;

import com.harsha.infytel_customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository  extends JpaRepository<Customer, Long> {
}
