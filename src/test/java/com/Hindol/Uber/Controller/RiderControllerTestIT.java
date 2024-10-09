package com.Hindol.Uber.Controller;

import com.Hindol.Uber.DTO.PointDTO;
import com.Hindol.Uber.DTO.RatingDTO;
import com.Hindol.Uber.DTO.RideRequestDTO;
import com.Hindol.Uber.Entity.Enum.PaymentMethod;
import com.Hindol.Uber.Entity.Enum.RideRequestStatus;
import com.Hindol.Uber.Entity.Enum.RideStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;


import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.*;

class RiderControllerTestIT extends AbstractIntegrationTest{
    private RatingDTO ratingDTO;

    private RideRequestDTO rideRequestDTO;
    @BeforeEach
    void setUp() {
        rideRequestDTO = RideRequestDTO.builder()
                .id(1L)
                .pickUpLocation(new PointDTO(new double[]{74.1213, 28.234123}))
                .dropOffLocation(new PointDTO(new double[]{74.221323, 28.33423123}))
                .requestedTime(LocalDateTime.now())
                .paymentMethod(PaymentMethod.WALLET)
                .build();
        ratingDTO = RatingDTO.builder()
                .rideId(1L)
                .rating(5)
                .build();
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
    void testGetMyProfile_whenSuccess() throws Exception {
        mockMvc.perform(get("/rider/getMyProfile"))
                .andExpect(jsonPath("$.data.user.email").value("rider@gmail.com"))
                .andExpect(jsonPath("$.data.user.name").value("Rider"));
    }

    @Test
    @WithUserDetails(value = "rider@gmail.com", userDetailsServiceBeanName = "userService")
    void testRateDriver_whenSuccess() throws Exception {
        mockMvc.perform(post("/rider/rateDriver")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ratingDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.rating").value(5));
    }

    @Test
    @WithUserDetails(value = "rider@gmail.com", userDetailsServiceBeanName = "userService")
    void testGetAllMyRides_whenSuccess() throws Exception {
        mockMvc.perform(get("/rider/getMyRides"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.*", hasSize(2)));
    }

    @Test
    @WithUserDetails(value = "rider@gmail.com", userDetailsServiceBeanName = "userService")
    void testCancelRide_whenSuccess() throws Exception {
        mockMvc.perform(post("/rider/cancelRide/{id}", 2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.rideStatus").value(RideStatus.CANCELLED.name()))
                .andExpect(jsonPath("$.data.driver.available").value(true));
    }
}