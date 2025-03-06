package com.harsha.infytel_customer.controller;

import com.harsha.infytel_customer.dto.CustomerDTO;
import com.harsha.infytel_customer.dto.LoginDTO;
import com.harsha.infytel_customer.dto.PlanDTO;
import com.harsha.infytel_customer.service.CustomerService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
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
//    @Value("${plan.Url}")
    String planUrl;
//    @Value("${friend.Url}")
    String friendUrl;
    @Autowired
    DiscoveryClient discoveryClient;
    @Autowired
    FriendFegin ff;

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
    // if there is a failure in circuit breaker control will go to fallback method
    @CircuitBreaker(name="customerService", fallbackMethod ="getCustomerProfileFallback")
    @GetMapping(value="/customers/{phoneNo}")
    public CustomerDTO getCustomerProfile(@PathVariable long phoneNo){
        long overAllStart = System.currentTimeMillis();
        System.out.println("Profile request for customer "+ phoneNo);
        CustomerDTO customerDTO = customerService.getCustomerProfile(phoneNo);
        //Fetching planUrl manually using discovery client
        List<ServiceInstance> planInstance = discoveryClient.getInstances("infytel-plan");

        if(planInstance != null && !planInstance.isEmpty()){
            planUrl = planInstance.get(0).getUri().toString();//fetches till port number
            System.out.println("Debug"+ planUrl);
        }

        //Fetching friendUrl manually using discovery client
        List<ServiceInstance> friendInstance = discoveryClient.getInstances("infytel-friend-family");
        if(friendInstance != null && !friendInstance.isEmpty()){
            friendUrl = friendInstance.get(0).getUri().toString();//fetches till port number
        }

        long planStart = System.currentTimeMillis();
        PlanDTO planDTO = new RestTemplate()
                .getForObject(planUrl+"/plans/"+ customerDTO.getCurrentPlan().getPlanId(), PlanDTO.class);
        long planEnd = System.currentTimeMillis();
        customerDTO.setCurrentPlan(planDTO);
        //@GetMapping("/customers/{phoneNo}/friends")
//        List<Long> friendList = new RestTemplate().getForObject(friendUrl+"/customers/"+phoneNo+"/friends", List.class);
        long friendStart = System.currentTimeMillis();
        List<Long> friendList = ff.getFriendList(phoneNo);
        long friendEnd = System.currentTimeMillis();
        customerDTO.setFriendAndFamily(friendList);
        long overAllEnd = System.currentTimeMillis();
        System.out.println("Total time for plan"+(planEnd-planStart));
        System.out.println("Total time for friend"+(friendEnd-friendStart));
        System.out.println(" Overall processing request time "+(overAllEnd-overAllStart));
        return customerDTO;
    }

    public CustomerDTO getCustomerProfileFallback(long phoneNo, Throwable throwable){
        System.out.println("------------Fallback Method----------------");
        return new CustomerDTO();
    }

}
