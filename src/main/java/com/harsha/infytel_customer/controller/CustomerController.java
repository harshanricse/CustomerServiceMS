package com.harsha.infytel_customer.controller;

import com.harsha.infytel_customer.dto.CustomerDTO;
import com.harsha.infytel_customer.dto.LoginDTO;
import com.harsha.infytel_customer.dto.PlanDTO;
import com.harsha.infytel_customer.service.CustomerService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${plan.Url}")
    String planUrl;
    @Value("${friend.Url}")
    String friendUrl;
    @Autowired
    DiscoveryClient discoveryClient;

    @PostMapping(value="/customers")
    public void createCustomer(@RequestBody CustomerDTO customerDTO){
        System.out.println("Creation request for customer"+ customerDTO);
        customerService.createCustomer(customerDTO);
    }
    @PostMapping(value="/login")
    public boolean login(@RequestBody LoginDTO loginDTO){
        System.out.println("Login request for customer "+loginDTO.getPhoneNo()+ " with password "+loginDTO.getPassword());
        return customerService.login(loginDTO);
    }

    @CircuitBreaker(name="customerService", fallbackMethod = "getCustomerProfileFallback")
    @GetMapping(value="/customers/{phoneNo}")
    public CustomerDTO getCustomerProfile(@PathVariable long phoneNo){
        System.out.println("Profile request for customer "+ phoneNo);
        CustomerDTO customerDTO = customerService.getCustomerProfile(phoneNo);

        List<ServiceInstance> planInstance = discoveryClient.getInstances("infytel-plan");
        if(planInstance != null && !planInstance.isEmpty()){
            planUrl = planInstance.get(0).getUri().toString();
        }

        PlanDTO planDTO = new RestTemplate()
                .getForObject(planUrl+"/plans/"+ customerDTO.getCurrentPlan().getPlanId(), PlanDTO.class);
        customerDTO.setCurrentPlan(planDTO);
        List<Long> friendList = new RestTemplate().getForObject(friendUrl+phoneNo+"/friends", List.class);
        customerDTO.setFriendAndFamily(friendList);
//        if(phoneNo==100){
//            throw new RuntimeException()
//        }
        return customerDTO;
    }

    public CustomerDTO getCustomerProfileFallback(Long phoneNo, Throwable throwable){
        System.out.println("------------Fallback Method----------------");
        return new CustomerDTO();
    }

}