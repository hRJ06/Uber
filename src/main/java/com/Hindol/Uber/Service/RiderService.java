package com.Hindol.Uber.Service;

import com.Hindol.Uber.DTO.DriverDTO;
import com.Hindol.Uber.DTO.RideDTO;
import com.Hindol.Uber.DTO.RideRequestDTO;
import com.Hindol.Uber.DTO.RiderDTO;
import com.Hindol.Uber.Entity.Rider;
import com.Hindol.Uber.Entity.User;

import java.util.List;

public interface RiderService {
    RideRequestDTO requestRide(RideRequestDTO rideRequestDTO);
    RideDTO cancelRide(Long rideId);
    RideDTO startRide(Long rideId);
    RideDTO endRide(Long rideId);
    DriverDTO rateDriver(Long rideId, Integer rating);
    RiderDTO getMyProfile();
    List<RideDTO> getAllMyRides();
    Rider createNewDriver(User user);
}
