package com.Hindol.Uber.Controller;

import com.Hindol.Uber.Entity.Driver;
import com.Hindol.Uber.Entity.Ride;
import com.Hindol.Uber.Entity.Rider;
import com.Hindol.Uber.Entity.User;
import com.Hindol.Uber.Repository.*;
import com.Hindol.Uber.TestContainerConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Import(TestContainerConfiguration.class)
public class AbstractIntegrationTest {

    @Autowired
    public MockMvc mockMvc;

    @Autowired
    public ObjectMapper objectMapper;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public RideRequestRepository rideRequestRepository;

    @Autowired
    public RideRepository rideRepository;

    @Autowired
    public RatingRepository ratingRepository;

    @Autowired
    public PaymentRepository paymentRepository;

    public User user;
    public User user_rider;  /* RIDER */
    public User user_driver; /* DRIVER */

    public Ride ride;

    public Rider rider;
    public Driver driver;

    public GeometryFactory geometryFactory;
    public Point pickUpLocation;
    public Point dropOffLocation;

}
