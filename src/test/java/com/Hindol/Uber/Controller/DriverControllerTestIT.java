package com.Hindol.Uber.Controller;

import com.Hindol.Uber.DTO.RatingDTO;
import com.Hindol.Uber.DTO.RideStartDTO;
import com.Hindol.Uber.Entity.Enum.RideStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.*;

class DriverControllerTestIT extends AbstractIntegrationTest{

    private RideStartDTO rideStartDTO;
    private RatingDTO ratingDTO;
    @BeforeEach
    void setUp() {
        rideStartDTO = RideStartDTO.builder()
                .otp("4567")
                .build();
        ratingDTO = RatingDTO.builder()
                .rideId(1L)
                .rating(5)
                .build();
    }

    @Test
    @WithUserDetails(value = "driver@gmail.com", userDetailsServiceBeanName = "userService")
    @Order(1)
    void testAcceptRide_whenSuccess() throws Exception {
        mockMvc.perform(post("/drivers/acceptRide/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.driver", notNullValue()))
                .andExpect(jsonPath("$.data.driver.available").value(false))
                .andExpect(jsonPath("$.data.rideStatus").value(RideStatus.CONFIRMED.name()));
    }

    @Test
    @WithUserDetails(value = "driver2@gmail.com", userDetailsServiceBeanName = "userService")
    @Order(2)
    void testStartRide_whenSuccess() throws Exception {
        mockMvc.perform(post("/drivers/startRide/{id}", 3)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(rideStartDTO))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.rideStatus").value(RideStatus.ONGOING.name()));
    }

    @Test
    @WithUserDetails(value = "driver2@gmail.com", userDetailsServiceBeanName = "userService")
    @Order(3)
    void testCancelRide_whenSuccess() throws Exception {
        mockMvc.perform(post("/drivers/cancelRide/{id}", 3)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.rideStatus").value(RideStatus.CANCELLED.name()));
    }

    @Test
    @WithUserDetails(value = "driver@gmail.com", userDetailsServiceBeanName = "userService")
    @Order(4)
    void testGetMyProfile_whenSuccess() throws Exception {
        mockMvc.perform(get("/drivers/getMyProfile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.user.name").value("Driver"))
                .andExpect(jsonPath("$.data.vehicleId").value("3897"));
    }

    @Test
    @WithUserDetails(value = "driver@gmail.com", userDetailsServiceBeanName = "userService")
    @Order(5)
    void testRateDriver_whenValidDriver_thenFailure() throws Exception {
            mockMvc.perform(post("/drivers/rateRider")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(ratingDTO)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.rating").value(5.0));
    }

    @Test
    @WithUserDetails(value = "driver2@gmail.com", userDetailsServiceBeanName = "userService")
    @Order(6)
    void testRateRider_whenInvalidDriver_thenFailure() throws Exception {
        mockMvc.perform(post("/drivers/rateRider")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ratingDTO)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.apiError.message").value("Driver is not the owner of ride"));
    }

    @Test
    @WithUserDetails(value = "driver@gmail.com", userDetailsServiceBeanName = "userService")
    @Order(7)
    void testGetAllMyRides_whenSuccess() throws Exception {
        mockMvc.perform(get("/drivers/getMyRides"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.*", hasSize(3)));
    }

    @Test
    @WithUserDetails(value = "driver@gmail.com", userDetailsServiceBeanName = "userService")
    @Order(8)
    void testEndRide_whenSuccess() throws Exception {
        mockMvc.perform(post("/drivers/endRide/{id}", 2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.driver", notNullValue()))
                .andExpect(jsonPath("$.data.driver.available").value(true))
                .andExpect(jsonPath("$.data.rideStatus").value(RideStatus.ENDED.name()));
    }

}