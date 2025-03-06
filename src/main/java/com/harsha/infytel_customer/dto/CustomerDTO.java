package com.harsha.infytel_customer.dto;

import com.harsha.infytel_customer.entity.Customer;
import com.harsha.infytel_customer.entity.Plan;
import lombok.Data;

import java.util.List;
@Data
public class CustomerDTO {
    private long phoneNo;
    private String name;
    private Integer age;
    private char gender;
    private List<Long> friendAndFamily;
    private String password;
    private String address;
    private PlanDTO currentPlan;

    //Converts Entity to DTO
    public static CustomerDTO valueOf(Customer cust){
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setAge(cust.getAge());
        customerDTO.setGender(cust.getGender());
        customerDTO.setName(cust.getName());
        customerDTO.setPhoneNo(cust.getPhoneNo());
        customerDTO.setPassword(cust.getPassword());
        customerDTO.setAddress(cust.getAddress());
        PlanDTO planDTO = new PlanDTO();
        planDTO.setPlanId(cust.getPlanId());
        customerDTO.setCurrentPlan(planDTO);
        return customerDTO;
    }
    //converts dto to entity
    public Customer createEntity(){
        Customer cust = new Customer();
        cust.setAge(this.getAge());
        cust.setGender(this.getGender());
        cust.setName(this.getName());
        cust.setPhoneNo(this.getPhoneNo());
        cust.setAddress(this.getPassword());
        cust.setPassword(this.getPassword());
        return cust;
    }
}
