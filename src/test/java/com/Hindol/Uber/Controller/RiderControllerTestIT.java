package com.Hindol.Uber.Controller;

import com.Hindol.Uber.DTO.PointDTO;
import com.Hindol.Uber.DTO.RatingDTO;
import com.Hindol.Uber.DTO.RideRequestDTO;
import com.Hindol.Uber.Entity.*;
import com.Hindol.Uber.Entity.Enum.PaymentMethod;
import com.Hindol.Uber.Entity.Enum.RideRequestStatus;
import com.Hindol.Uber.Entity.Enum.RideStatus;
import com.Hindol.Uber.Entity.Enum.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
class RiderControllerTestIT extends AbstractIntegrationTest{

    private RatingDTO ratingDTO;
    private RideRequestDTO rideRequestDTO;
    private Rating rating;

    @BeforeEach
    void setUp() {
        user_rider = User.builder()
                .id(1L)
                .name("Rider")
                .email("rider@gmail.com")
                .password("ride")
                .roles(Set.of(Role.RIDER))
                .build();
        user_driver = User.builder()
                .id(2L)
                .name("Driver")
                .email("driver@gmail.com")
                .password("drive")
                .roles(Set.of(Role.RIDER, Role.DRIVER))
                .build();
        rider = Rider.builder()
                .id(1L)
                .user(user_rider)
                .rating(0.0)
                .build();
        driver = Driver.builder()
                .id(1L)
                .user(user_driver)
                .rating(0.0)
                .build();
        rideRequestDTO = RideRequestDTO.builder()
                .pickUpLocation(new PointDTO(new double[]{74.1213, 28.234123}))
                .dropOffLocation(new PointDTO(new double[]{74.221323, 28.33423123}))
                .requestedTime(LocalDateTime.now())
                .paymentMethod(PaymentMethod.WALLET)
                .build();

        geometryFactory = new GeometryFactory();

        pickUpLocation = geometryFactory.createPoint(new Coordinate(74.1213, 28.234123));
        pickUpLocation.setSRID(4326);

        dropOffLocation = geometryFactory.createPoint(new Coordinate(74.221323, 28.33423123));
        dropOffLocation.setSRID(4326);

        ride = Ride.builder()
                .pickUpLocation(pickUpLocation)
                .dropOffLocation(dropOffLocation)
                .rider(rider)
                .driver(driver)
                .paymentMethod(PaymentMethod.CASH)
                .rideStatus(RideStatus.CONFIRMED)
                .otp("1234")
                .fare(300.00)
                .startedAt(LocalDateTime.now())
                .build();

        rating = Rating.builder()
                .ride(ride)
                .rider(rider)
                .driver(driver)
                .build();

        ratingDTO = RatingDTO.builder()
                .rideId(1L)
                .rating(5)
                .build();

        paymentRepository.deleteAll();
        ratingRepository.deleteAll();
        rideRepository.deleteAll();
        rideRequestRepository.deleteAll();
    }


    @Test
    /* @WithMockUser(username = "admin@gmail.com", roles = {"RIDER"}) */
    @WithUserDetails(value = "rider@gmail.com", userDetailsServiceBeanName = "userService")
    void testRequestRide_whenSuccess() throws Exception {
        mockMvc.perform(post("/rider/requestRide")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rideRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.rider", notNullValue()))
                .andExpect(jsonPath("$.data.fare").isNumber())
                .andExpect(jsonPath("$.data.paymentMethod").value(PaymentMethod.WALLET.name()))
                .andExpect(jsonPath("$.data.rideRequestStatus").value(RideRequestStatus.PENDING.name()));
    }

    @Test
    @WithUserDetails(value = "rider@gmail.com", userDetailsServiceBeanName = "userService")
    void testCancelRide_whenSuccess() throws Exception {
        Ride mockRide = rideRepository.save(ride);
        mockMvc.perform(post("/rider/cancelRide/{id}", mockRide.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(mockRide.getId()))
                .andExpect(jsonPath("$.data.rideStatus").value(RideStatus.CANCELLED.name()))
                .andExpect(jsonPath("$.data.driver.available").value(true));
    }

    @Test
    @WithUserDetails(value = "rider@gmail.com", userDetailsServiceBeanName = "userService")
    void testRateDriver_whenSuccess() throws Exception {
        /* Arrange */
        ride.setRideStatus(RideStatus.ENDED);
        Ride mockRide = rideRepository.save(ride);
        ratingRepository.save(rating);
        ratingDTO.setRideId(mockRide.getId());


        mockMvc.perform(post("/rider/rateDriver")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ratingDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.rating").value(5));
    }

    @Test
    @WithUserDetails(value = "rider@gmail.com", userDetailsServiceBeanName = "userService")
    void testGetMyProfile_whenSuccess() throws Exception {
        mockMvc.perform(get("/rider/getMyProfile"))
                .andExpect(jsonPath("$.data.user.email").value("rider@gmail.com"))
                .andExpect(jsonPath("$.data.user.name").value("Rider"));
    }

    @Test
    @WithUserDetails(value = "rider@gmail.com", userDetailsServiceBeanName = "userService")
    void testGetAllMyRides_whenSuccess() throws Exception {
        rideRepository.save(ride);
        mockMvc.perform(get("/rider/getMyRides"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.*", hasSize(1)));
    }

}