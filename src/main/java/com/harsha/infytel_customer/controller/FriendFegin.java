package com.harsha.infytel_customer.controller;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(name="infytel-friend-family", url = "http://localhost:8082")

public interface FriendFegin {
    @RequestMapping(value = "/customers/{phoneNo}/friends")
    List<Long> getFriendList(@PathVariable("phoneNo") Long phoneNo);
}
