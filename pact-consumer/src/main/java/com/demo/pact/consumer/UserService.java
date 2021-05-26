package com.demo.pact.consumer;

import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserClient userClient;

    public UserService(UserClient userClient) {
        this.userClient = userClient;
    }

    User getUser(Long id){
        return userClient.getUser(id);
    }
}
