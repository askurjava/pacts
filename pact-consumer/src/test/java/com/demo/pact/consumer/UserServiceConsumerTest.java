package com.demo.pact.consumer;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.model.RequestResponsePact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(PactConsumerTestExt.class)
@ExtendWith(SpringExtension.class)
@PactTestFor(providerName = "userservice", port = "8888")
@SpringBootTest({"user.service.base.url:http://localhost:8888"}) // In case of feign client
class UserServiceConsumerTest {

    @Autowired
    private UserClient userClient;

    @Pact(state = "provider accepts a new person", provider = "userservice", consumer = "userclient")
    RequestResponsePact createPersonPact(PactDslWithProvider builder) {
        Map<String, String> headers = new HashMap();
        headers.put("Content-Type", "application/json");
        return builder
                .given("provider accepts a new person")
                .uponReceiving("a request to POST a person")
                .path("/user-service/users")
                .headers(headers)
                .method("POST")
                .body(new PactDslJsonBody()
                        .stringType("firstName", "shubham")
                        .stringType("lastName", "shukla"))
                .willRespondWith()
                .status(201)
                .matchHeader("Content-Type", "application/json")
                .body(new PactDslJsonBody()
                        .integerType("id", 42))
                .toPact();
    }

    @Test
    void verifyCreatePersonPact() {
        User user = new User();
        user.setFirstName("shubham");
        user.setLastName("shukla");
        IdObject id = userClient.createUser(user);
        assertThat(id.getId()).isEqualTo(42);
    }
}
