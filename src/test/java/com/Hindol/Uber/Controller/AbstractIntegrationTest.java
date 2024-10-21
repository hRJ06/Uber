package com.Hindol.Uber.Controller;

import com.Hindol.Uber.Entity.Driver;
import com.Hindol.Uber.Entity.User;
import com.Hindol.Uber.Repository.UserRepository;
import com.Hindol.Uber.TestContainerConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Import(TestContainerConfiguration.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AbstractIntegrationTest {

    @Autowired
    public MockMvc mockMvc;

    @Autowired
    public ObjectMapper objectMapper;

    @Autowired
    public UserRepository userRepository;

    public User user;
    public User user_rider;  /* RIDER */
    public User user_driver; /* DRIVER */

}
