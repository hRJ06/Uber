package com.Hindol.Uber.Service;

import com.Hindol.Uber.DTO.DriverDTO;
import com.Hindol.Uber.DTO.RideDTO;
import com.Hindol.Uber.DTO.RiderDTO;

import java.util.List;

public interface DriverService {
    RideDTO acceptRide(Long rideId);
    RideDTO cancelRide(Long rideId);
    RideDTO startRide(Long rideId);
    RideDTO endRide(Long rideId);
    RiderDTO rateRider(Long rideId,Integer rating);
    DriverDTO getMyProfile();
    List<RideDTO> getAllMyRides();
}
