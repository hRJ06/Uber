package com.Hindol.Uber.Controller;

import com.Hindol.Uber.DTO.RatingDTO;
import com.Hindol.Uber.DTO.RideStartDTO;
import com.Hindol.Uber.Entity.*;
import com.Hindol.Uber.Entity.Enum.PaymentMethod;
import com.Hindol.Uber.Entity.Enum.RideRequestStatus;
import com.Hindol.Uber.Entity.Enum.RideStatus;
import com.Hindol.Uber.Entity.Enum.Role;
import com.Hindol.Uber.Repository.WalletTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
class DriverControllerTestIT extends AbstractIntegrationTest{
    @Autowired
    private WalletTransactionRepository walletTransactionRepository;
    private RideStartDTO rideStartDTO;
    private RatingDTO ratingDTO;
    private RideRequest rideRequest;
    private Payment payment;
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

        geometryFactory = new GeometryFactory();

        pickUpLocation = geometryFactory.createPoint(new Coordinate(12.9716, 77.5946));
        pickUpLocation.setSRID(4326);

        dropOffLocation = geometryFactory.createPoint(new Coordinate(74.221323, 28.33423123));
        dropOffLocation.setSRID(4326);

        rideRequest = RideRequest.builder()
                .rider(rider)
                .pickUpLocation(pickUpLocation)
                .dropOffLocation(dropOffLocation)
                .requestedTime(LocalDateTime.now())
                .paymentMethod(PaymentMethod.WALLET)
                .rideRequestStatus(RideRequestStatus.PENDING)
                .fare(300.00)
                .build();

        ride = Ride.builder()
                .id(1L)
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

        rideStartDTO = RideStartDTO.builder()
                .otp("1234")
                .build();
        ratingDTO = RatingDTO.builder()
                .rideId(1L)
                .rating(5)
                .build();
        payment = Payment.builder()
                        .paymentMethod(PaymentMethod.CASH)
                        .ride(ride)
                        .amount(300.00)
                        .build();
        rating = Rating.builder()
                .ride(ride)
                .rider(rider)
                .driver(driver)
                .build();

        walletTransactionRepository.deleteAll();
        paymentRepository.deleteAll();
        ratingRepository.deleteAll();
        rideRepository.deleteAll();
        rideRequestRepository.deleteAll();
    }

    @Test
    @WithUserDetails(value = "driver@gmail.com", userDetailsServiceBeanName = "userService")
    void testAcceptRide_whenSuccess() throws Exception {
        RideRequest mockRideRequest = rideRequestRepository.save(rideRequest);
        mockMvc.perform(post("/drivers/acceptRide/{id}", mockRideRequest.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.driver", notNullValue()))
                .andExpect(jsonPath("$.data.driver.available").value(false))
                .andExpect(jsonPath("$.data.rideStatus").value(RideStatus.CONFIRMED.name()));
    }

    @Test
    @WithUserDetails(value = "driver@gmail.com", userDetailsServiceBeanName = "userService")
    void testStartRide_whenSuccess() throws Exception {
        Ride mockRide = rideRepository.save(ride);
        mockMvc.perform(post("/drivers/startRide/{id}", mockRide.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(rideStartDTO))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.rideStatus").value(RideStatus.ONGOING.name()));
    }
    @Test
    @WithUserDetails(value = "driver@gmail.com", userDetailsServiceBeanName = "userService")
    void testEndRide_whenSuccess() throws Exception {
        /* Arrange */
        ride.setRideStatus(RideStatus.ONGOING);
        Ride mockRide = rideRepository.save(ride);
        payment.setRide(mockRide);
        paymentRepository.save(payment);

        mockMvc.perform(post("/drivers/endRide/{id}", mockRide.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.driver", notNullValue()))
                .andExpect(jsonPath("$.data.driver.available").value(true))
                .andExpect(jsonPath("$.data.rideStatus").value(RideStatus.ENDED.name()));
    }

    @Test
    @WithUserDetails(value = "driver@gmail.com", userDetailsServiceBeanName = "userService")
    void testCancelRide_whenSuccess() throws Exception {
        /* Arrange */
        ride.setRideStatus(RideStatus.CONFIRMED);
        Ride mockRide = rideRepository.save(ride);
        mockMvc.perform(post("/drivers/cancelRide/{id}", mockRide.getId())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.rideStatus").value(RideStatus.CANCELLED.name()));
    }

    @Test
    @WithUserDetails(value = "driver@gmail.com", userDetailsServiceBeanName = "userService")
    void testGetMyProfile_whenSuccess() throws Exception {
        mockMvc.perform(get("/drivers/getMyProfile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.user.name").value("Driver"))
                .andExpect(jsonPath("$.data.vehicleId").value("3897"));
    }

    @Test
    @WithUserDetails(value = "driver@gmail.com", userDetailsServiceBeanName = "userService")
    void testRateRider_whenValidDriver_thenFailure() throws Exception {
            /* Arrange */
            ride.setRideStatus(RideStatus.ENDED);
            rideRepository.save(ride);
            ratingRepository.save(rating);

            mockMvc.perform(post("/drivers/rateRider")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(ratingDTO)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.rating").value(5.0));
    }

    @Test
    @WithUserDetails(value = "driver2@gmail.com", userDetailsServiceBeanName = "userService")
    void testRateRider_whenInvalidDriver_thenFailure() throws Exception {
        /* Arrange */
        Ride mockRide = rideRepository.save(ride);
        ratingDTO.setRideId(mockRide.getId());

        mockMvc.perform(post("/drivers/rateRider")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ratingDTO)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.apiError.message").value("Driver is not the owner of ride"));
    }

    @Test
    @WithUserDetails(value = "driver@gmail.com", userDetailsServiceBeanName = "userService")
    void testGetAllMyRides_whenSuccess() throws Exception {
        rideRepository.save(ride);
        mockMvc.perform(get("/drivers/getMyRides"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.*", hasSize(1)));
    }

}