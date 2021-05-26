package com.demo.pact.consumer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private final UserService service;

    public TestController(UserService service) {
        this.service = service;
    }

    @GetMapping("/user/{id}")
    String getUserName(@PathVariable  Long id){
        User user = service.getUser(id);
         return user.getFirstName()+ " " + user.getLastName() ;
    }
}
