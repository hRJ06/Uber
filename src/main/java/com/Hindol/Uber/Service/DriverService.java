package com.Hindol.Uber.Service;

import com.Hindol.Uber.DTO.DriverDTO;
import com.Hindol.Uber.DTO.RideDTO;
import com.Hindol.Uber.DTO.RiderDTO;
import com.Hindol.Uber.Entity.Driver;

import java.util.List;

public interface DriverService {
    RideDTO acceptRide(Long rideRequestId);
    RideDTO cancelRide(Long rideId);
    RideDTO startRide(Long rideId, String otp);
    RideDTO endRide(Long rideId);
    RiderDTO rateRider(Long rideId,Integer rating);
    DriverDTO getMyProfile();
    List<RideDTO> getAllMyRides();
    Driver getCurrentDriver();
}
