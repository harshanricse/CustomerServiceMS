package com.harsha.infytel_customer.service;

import com.harsha.infytel_customer.dto.CustomerDTO;
import com.harsha.infytel_customer.dto.LoginDTO;
import com.harsha.infytel_customer.entity.Customer;
import com.harsha.infytel_customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public void createCustomer(CustomerDTO customerDTO){
        System.out.println("Creation request for customer"+ customerDTO);
        Customer cust = customerDTO.createEntity();
        customerRepository.save(cust);
    }

    public boolean login(LoginDTO loginDTO){
        Customer cust = null;
        System.out.println("Login request for customer"+loginDTO.getPhoneNo()+" with password "+ loginDTO.getPassword());
        Optional<Customer> optCust = customerRepository.findById(loginDTO.getPhoneNo());
        if(optCust.isPresent()){
            cust = optCust.get();
            if(cust.getPassword().equals(loginDTO.getPassword())){
                return true;
            }
        }
        return false;
    }

    // fetches full profile of a specific customer
    public CustomerDTO getCustomerProfile(long phoneNo){
        CustomerDTO customerDTO = null;
        System.out.println("Profile request for customer"+ phoneNo);
        Optional<Customer> optionalCustomer = customerRepository.findById(phoneNo);
        if(optionalCustomer.isPresent()){
            Customer cust = optionalCustomer.get();
            customerDTO = CustomerDTO.valueOf(cust);
        }
        System.out.println("Profile for customer: "+customerDTO);
        return customerDTO;
    }

}
